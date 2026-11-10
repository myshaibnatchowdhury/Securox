package com.securox.antitheft.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.security.DeviceOwnerManager
import com.securox.antitheft.security.DeviceSecurityManager
import java.util.concurrent.TimeUnit

class HeartbeatWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val prefs = EncryptedPreferencesManager(context)
        val securityManager = DeviceSecurityManager(context)
        val ownerManager = DeviceOwnerManager(context)

        val deviceId = DeviceInfoHelper.getDeviceId(context)
        val battery = DeviceInfoHelper.getBatteryLevel(context)
        val charging = DeviceInfoHelper.isCharging(context)
        val adminActive = securityManager.isAdminActive()
        val ownerActive = ownerManager.isDeviceOwner()

        val devices = prefs.getDevices().toMutableList()
        val index = devices.indexOfFirst { it.deviceId == deviceId }

        val status = when {
            adminActive && ownerActive -> "PROTECTED"
            adminActive || ownerActive -> "PARTIALLY_PROTECTED"
            else -> "UNPROTECTED"
        }

        if (index >= 0) {
            devices[index] = devices[index].copy(
                batteryLevel = battery,
                isCharging = charging,
                lastSeen = System.currentTimeMillis(),
                isAdminActive = adminActive,
                isDeviceOwner = ownerActive,
                protectionStatus = status,
                isOnline = true
            )
            prefs.saveDevices(devices)
        }

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "securox_heartbeat_sync_work"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<HeartbeatWorker>(
                AppConstants.HEARTBEAT_INTERVAL_MINUTES,
                TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
