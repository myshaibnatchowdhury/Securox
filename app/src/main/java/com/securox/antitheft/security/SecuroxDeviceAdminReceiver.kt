package com.securox.antitheft.security

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityEvent
import java.util.UUID

class SecuroxDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "SECUROX Administrator Mode Activated", Toast.LENGTH_SHORT).show()

        val prefs = EncryptedPreferencesManager(context)
        prefs.addEvent(
            SecurityEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                deviceId = DeviceInfoHelper.getDeviceId(context),
                deviceName = DeviceInfoHelper.getDeviceName(),
                ownerUid = prefs.currentUser?.uid ?: "local_owner",
                eventType = AppConstants.EVENT_ADMIN_ENABLED,
                title = "Device Administrator Activated",
                description = "SECUROX Anti-Theft has been granted Device Administrator privileges.",
                severity = "INFO"
            )
        )
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        NotificationHelper.showSecurityAlert(
            context,
            2001,
            "WARNING: Administrator Deactivated",
            "SECUROX Device Administrator privileges have been removed. Remote emergency lock is now disabled."
        )

        val prefs = EncryptedPreferencesManager(context)
        prefs.addEvent(
            SecurityEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                deviceId = DeviceInfoHelper.getDeviceId(context),
                deviceName = DeviceInfoHelper.getDeviceName(),
                ownerUid = prefs.currentUser?.uid ?: "local_owner",
                eventType = AppConstants.EVENT_ADMIN_DISABLED,
                title = "Device Administrator Deactivated",
                description = "Critical protection alert: Administrator privileges revoked.",
                severity = "CRITICAL"
            )
        )
    }

    override fun onPasswordFailed(context: Context, intent: Intent) {
        super.onPasswordFailed(context, intent)
        NotificationHelper.showSecurityAlert(
            context,
            2002,
            "Security Alert: Failed Unlock Attempt",
            "An unauthorized person attempted to unlock your device."
        )

        val prefs = EncryptedPreferencesManager(context)
        prefs.addEvent(
            SecurityEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                deviceId = DeviceInfoHelper.getDeviceId(context),
                deviceName = DeviceInfoHelper.getDeviceName(),
                ownerUid = prefs.currentUser?.uid ?: "local_owner",
                eventType = "PASSWORD_ATTEMPT_FAILED",
                title = "Failed Passcode Attempt",
                description = "Incorrect unlock attempt detected on protected device.",
                severity = "WARNING"
            )
        )
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent) {
        super.onPasswordSucceeded(context, intent)
    }
}
