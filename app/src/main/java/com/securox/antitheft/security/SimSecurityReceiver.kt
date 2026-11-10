package com.securox.antitheft.security

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityEvent
import java.util.UUID

class SimSecurityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == "android.intent.action.SIM_STATE_CHANGED") {
            val stateExtra = intent.getStringExtra("ss") ?: ""
            val prefs = EncryptedPreferencesManager(context)
            val deviceId = DeviceInfoHelper.getDeviceId(context)
            val deviceName = DeviceInfoHelper.getDeviceName()

            val (severity, desc) = when (stateExtra) {
                "ABSENT" -> {
                    Pair("CRITICAL", "SIM card removed from device. Potential physical theft in progress.")
                }
                "LOCKED" -> {
                    Pair("WARNING", "SIM card PIN or PUK locked.")
                }
                "READY" -> {
                    Pair("INFO", "SIM card detected and operational.")
                }
                else -> {
                    Pair("INFO", "SIM state changed to: $stateExtra")
                }
            }

            if (stateExtra == "ABSENT") {
                NotificationHelper.showSecurityAlert(
                    context,
                    5001,
                    "THEFT ALERT: SIM Card Removed",
                    desc
                )
            }

            prefs.addEvent(
                SecurityEvent(
                    id = "evt_${UUID.randomUUID().toString().take(8)}",
                    deviceId = deviceId,
                    deviceName = deviceName,
                    ownerUid = prefs.currentUser?.uid ?: "local_owner",
                    eventType = AppConstants.EVENT_SIM_STATE_CHANGED,
                    title = "SIM Hardware Event: $stateExtra",
                    description = desc,
                    severity = severity
                )
            )
        }
    }
}
