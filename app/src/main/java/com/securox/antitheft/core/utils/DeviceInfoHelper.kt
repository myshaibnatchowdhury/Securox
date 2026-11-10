package com.securox.antitheft.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings

object DeviceInfoHelper {

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown_device_${Build.SERIAL}"
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        val model = Build.MODEL
        return if (model.startsWith(manufacturer, ignoreCase = true)) {
            model
        } else {
            "$manufacturer $model"
        }
    }

    fun getManufacturer(): String = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }

    fun getModel(): String = Build.MODEL

    fun getAndroidVersion(): String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    fun getBatteryLevel(context: Context): Int {
        val batteryIntent: Intent? = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        return batteryIntent?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level >= 0 && scale > 0) {
                (level * 100 / scale)
            } else 100
        } ?: 100
    }

    fun isCharging(context: Context): Boolean {
        val batteryIntent: Intent? = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        return batteryIntent?.let { intent ->
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
        } ?: false
    }
}
