const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.firestore();

/**
 * Triggered whenever an owner issues a new device security command (e.g. EMERGENCY LOCK, LOCATE).
 * Validates ownership, checks expiration, and dispatches high-priority FCM message to target device.
 */
exports.onDeviceCommandCreated = functions.firestore
  .document("device_commands/{commandId}")
  .onCreate(async (snap, context) => {
    const commandId = context.params.commandId;
    const command = snap.data();

    if (!command) {
      console.error(`Command data missing for ${commandId}`);
      return null;
    }

    const { ownerUid, targetDeviceId, commandType, expiresAt } = command;

    // Check expiration
    const now = Date.now();
    if (expiresAt && expiresAt < now) {
      console.warn(`Command ${commandId} expired before dispatch.`);
      return snap.ref.update({
        status: "EXPIRED",
        executionResult: "Command expired before transmission",
        updatedAt: now,
      });
    }

    // Lookup target device
    const deviceRef = db.collection("devices").doc(targetDeviceId);
    const deviceDoc = await deviceRef.get();

    if (!deviceDoc.exists) {
      console.error(`Target device ${targetDeviceId} not found.`);
      return snap.ref.update({
        status: "FAILED",
        executionResult: "Target device not registered in account",
        updatedAt: now,
      });
    }

    const deviceData = deviceDoc.data();
    if (deviceData.ownerUid !== ownerUid) {
      console.error(`Ownership mismatch for command ${commandId}`);
      return snap.ref.update({
        status: "FAILED",
        executionResult: "Authorization failed: Target device does not belong to owner",
        updatedAt: now,
      });
    }

    const fcmToken = deviceData.fcmToken;
    if (!fcmToken) {
      console.warn(`Device ${targetDeviceId} has no registered FCM token.`);
      return snap.ref.update({
        status: "PENDING_DELIVERY",
        executionResult: "Target device currently offline or unregistered token",
        updatedAt: now,
      });
    }

    // Build High Priority Data Message for background command execution
    const message = {
      token: fcmToken,
      data: {
        commandId: commandId,
        commandType: commandType,
        targetDeviceId: targetDeviceId,
        timestamp: String(now),
        priority: "HIGH",
      },
      android: {
        priority: "high",
        ttl: 60 * 5, // 5 minutes TTL
      },
    };

    try {
      const response = await admin.messaging().send(message);
      console.log(`Successfully dispatched FCM message for command ${commandId}: ${response}`);

      await snap.ref.update({
        status: "DELIVERED",
        deliveredAt: now,
      });

      // Log Security Event
      await db.collection("security_events").add({
        deviceId: targetDeviceId,
        ownerUid: ownerUid,
        eventType: "COMMAND_DISPATCHED",
        details: `Remote command ${commandType} transmitted to device`,
        severity: "INFO",
        timestamp: now,
      });

      return { success: true };
    } catch (error) {
      console.error(`FCM dispatch failed for command ${commandId}:`, error);
      return snap.ref.update({
        status: "DELIVERY_FAILED",
        executionResult: error.message || "FCM transmission error",
        updatedAt: now,
      });
    }
  });

/**
 * Triggered when target device reports command execution result (e.g. EXECUTED or FAILED).
 * Notifies the owner device with a confirmation notification.
 */
exports.onDeviceCommandUpdated = functions.firestore
  .document("device_commands/{commandId}")
  .onUpdate(async (change, context) => {
    const after = change.after.data();
    const before = change.before.data();
    const commandId = context.params.commandId;

    if (!after || !before) return null;

    // Only react when status changes to EXECUTED or FAILED
    if (before.status !== after.status && (after.status === "EXECUTED" || after.status === "FAILED")) {
      const { ownerUid, targetDeviceId, commandType, status, executionResult } = after;

      // Find owner devices to send confirmation push
      const ownerDevicesQuery = await db.collection("devices")
        .where("ownerUid", "==", ownerUid)
        .where("onlineStatus", "==", "ONLINE")
        .get();

      const ownerTokens = [];
      ownerDevicesQuery.forEach(doc => {
        const data = doc.data();
        if (doc.id !== targetDeviceId && data.fcmToken) {
          ownerTokens.push(data.fcmToken);
        }
      });

      if (ownerTokens.length > 0) {
        const title = status === "EXECUTED" ? "Command Confirmed" : "Command Failed";
        const body = `Command ${commandType} execution ${status.toLowerCase()}: ${executionResult || ""}`;

        await admin.messaging().sendMulticast({
          tokens: ownerTokens,
          notification: {
            title: title,
            body: body,
          },
          data: {
            commandId: commandId,
            status: status,
            targetDeviceId: targetDeviceId,
          },
        });
      }

      // Record in security audit log
      await db.collection("security_events").add({
        deviceId: targetDeviceId,
        ownerUid: ownerUid,
        eventType: `COMMAND_${status}`,
        details: `${commandType} ${status}: ${executionResult || ""}`,
        severity: status === "EXECUTED" ? "HIGH" : "WARNING",
        timestamp: Date.now(),
      });
    }

    return null;
  });

/**
 * Scheduled function running every 10 minutes to detect unexpectedly disconnected devices.
 */
exports.detectOfflineDevices = functions.pubsub
  .schedule("every 10 minutes")
  .onRun(async (context) => {
    const tenMinutesAgo = Date.now() - (10 * 60 * 1000);

    const staleDevices = await db.collection("devices")
      .where("onlineStatus", "==", "ONLINE")
      .where("lastSeen", "<", tenMinutesAgo)
      .get();

    const batch = db.batch();
    const alertPromises = [];

    staleDevices.forEach(doc => {
      const dev = doc.data();
      batch.update(doc.ref, { onlineStatus: "OFFLINE" });

      alertPromises.push(
        db.collection("security_events").add({
          deviceId: doc.id,
          ownerUid: dev.ownerUid,
          eventType: "DEVICE_OFFLINE_UNEXPECTED",
          details: `Device ${dev.deviceName || doc.id} has not reported heartbeat for over 10 minutes`,
          severity: "WARNING",
          timestamp: Date.now(),
        })
      );
    });

    await batch.commit();
    await Promise.all(alertPromises);
    console.log(`Updated ${staleDevices.size} stale devices to OFFLINE.`);
    return null;
  });
