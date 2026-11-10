package com.securox.antitheft.security

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.workers.HeartbeatWorker
import java.util.UUID

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            val prefs = EncryptedPreferencesManager(context)
            val deviceId = DeviceInfoHelper.getDeviceId(context)
            val deviceName = DeviceInfoHelper.getDeviceName()

            NotificationHelper.showSecurityAlert(
                context,
                4001,
                "SECUROX Shield Restored",
                "Device reboot detected. Anti-theft defense modules automatically re-engaged."
            )

            prefs.addEvent(
                SecurityEvent(
                    id = "evt_${UUID.randomUUID().toString().take(8)}",
                    deviceId = deviceId,
                    deviceName = deviceName,
                    ownerUid = prefs.currentUser?.uid ?: "local_owner",
                    eventType = AppConstants.EVENT_UNEXPECTED_REBOOT,
                    title = "Device System Booted",
                    description = "Phone completed startup sequence. Hardware telemetry refreshed and synchronized.",
                    severity = "INFO"
                )
            )

            // Re-schedule background heartbeat sync worker
            HeartbeatWorker.schedule(context)
        }
    }
}
