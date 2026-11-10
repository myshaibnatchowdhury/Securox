package com.securox.antitheft.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.location.SecuroxLocationService
import com.securox.antitheft.security.DeviceSecurityManager
import java.util.UUID

class SecuroxCommandReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val commandType = intent.getStringExtra("commandType") ?: return
        val commandId = intent.getStringExtra("commandId") ?: UUID.randomUUID().toString()
        val targetDeviceId = intent.getStringExtra("targetDeviceId") ?: DeviceInfoHelper.getDeviceId(context)

        val prefs = EncryptedPreferencesManager(context)
        val securityManager = DeviceSecurityManager(context)

        when (commandType) {
            AppConstants.CMD_LOCK_DEVICE -> {
                val locked = securityManager.lockDevice()
                val resultMsg = if (locked) {
                    "Emergency lock executed successfully via DevicePolicyManager lockNow()"
                } else {
                    "Lock command received but Device Administrator is not activated"
                }

                NotificationHelper.showSecurityAlert(
                    context,
                    6001,
                    "SECUROX EMERGENCY LOCK",
                    resultMsg
                )

                prefs.addCommand(
                    SecurityCommand(
                        commandId = commandId,
                        ownerUid = prefs.currentUser?.uid ?: "local_owner",
                        targetDeviceId = targetDeviceId,
                        commandType = commandType,
                        status = if (locked) AppConstants.STATUS_EXECUTED else AppConstants.STATUS_FAILED,
                        executionResult = resultMsg,
                        executedAt = System.currentTimeMillis()
                    )
                )

                prefs.addEvent(
                    SecurityEvent(
                        id = "evt_${UUID.randomUUID().toString().take(8)}",
                        deviceId = targetDeviceId,
                        deviceName = DeviceInfoHelper.getDeviceName(),
                        ownerUid = prefs.currentUser?.uid ?: "local_owner",
                        eventType = AppConstants.EVENT_LOCK_COMPLETED,
                        title = "Remote Lock Executed",
                        description = resultMsg,
                        severity = if (locked) "CRITICAL" else "WARNING"
                    )
                )
            }

            AppConstants.CMD_LOCATE_DEVICE -> {
                SecuroxLocationService.start(context)
                NotificationHelper.showSecurityAlert(
                    context,
                    6002,
                    "Remote Locate Command",
                    "Emergency high-accuracy GPS tracking initiated by owner."
                )
            }

            AppConstants.CMD_REFRESH_DEVICE_STATUS -> {
                val battery = DeviceInfoHelper.getBatteryLevel(context)
                NotificationHelper.showSecurityAlert(
                    context,
                    6003,
                    "Status Refreshed",
                    "Telemetry reported: Battery $battery%, Admin active: ${securityManager.isAdminActive()}"
                )
            }
        }
    }
}
