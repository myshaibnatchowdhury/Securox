package com.securox.antitheft.security

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityEvent
import java.util.UUID

class AntiShutdownGuard : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = EncryptedPreferencesManager(context)
        val deviceId = DeviceInfoHelper.getDeviceId(context)
        val deviceName = DeviceInfoHelper.getDeviceName()
        val batteryLevel = DeviceInfoHelper.getBatteryLevel(context)

        when (intent.action) {
            Intent.ACTION_POWER_DISCONNECTED -> {
                prefs.addEvent(
                    SecurityEvent(
                        id = "evt_${UUID.randomUUID().toString().take(8)}",
                        deviceId = deviceId,
                        deviceName = deviceName,
                        ownerUid = prefs.currentUser?.uid ?: "local_owner",
                        eventType = AppConstants.EVENT_POWER_DISCONNECTED,
                        title = "Power Cable Disconnected",
                        description = "External power removed. Battery level: $batteryLevel%. Hardware guard active.",
                        severity = "INFO"
                    )
                )
            }
            Intent.ACTION_BATTERY_LOW -> {
                NotificationHelper.showSecurityAlert(
                    context,
                    3001,
                    "CRITICAL: Battery Low Alert",
                    "Battery dropped to $batteryLevel%. Telemetry and last known location preserved."
                )

                prefs.addEvent(
                    SecurityEvent(
                        id = "evt_${UUID.randomUUID().toString().take(8)}",
                        deviceId = deviceId,
                        deviceName = deviceName,
                        ownerUid = prefs.currentUser?.uid ?: "local_owner",
                        eventType = AppConstants.EVENT_BATTERY_LOW,
                        title = "Critical Low Battery Warning",
                        description = "Device battery critically low ($batteryLevel%). Emergency telemetry captured.",
                        severity = "WARNING"
                    )
                )
            }
        }
    }
}
