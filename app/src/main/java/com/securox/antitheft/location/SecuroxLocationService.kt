package com.securox.antitheft.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.SecurityEvent
import java.util.UUID

class SecuroxLocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isTracking = false

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    handleNewLocation(location)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START
        when (action) {
            ACTION_START -> startForegroundTracking()
            ACTION_STOP -> stopForegroundTracking()
        }
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startForegroundTracking() {
        if (isTracking) return
        isTracking = true

        NotificationHelper.createNotificationChannels(this)
        val notification = NotificationHelper.buildLocationServiceNotification(this)
        startForeground(AppConstants.LOCATION_NOTIFICATION_ID, notification)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 15000L)
            .setMinUpdateIntervalMillis(10000L)
            .setMinUpdateDistanceMeters(10f)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            stopForegroundTracking()
        }
    }

    private fun handleNewLocation(location: Location) {
        val prefs = EncryptedPreferencesManager(this)
        val deviceId = DeviceInfoHelper.getDeviceId(this)
        val devices = prefs.getDevices().toMutableList()
        val index = devices.indexOfFirst { it.deviceId == deviceId }

        if (index >= 0) {
            devices[index] = devices[index].copy(
                lastLatitude = location.latitude,
                lastLongitude = location.longitude,
                lastAccuracy = location.accuracy,
                lastLocationTime = System.currentTimeMillis()
            )
            prefs.saveDevices(devices)
        }

        prefs.addEvent(
            SecurityEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                deviceId = deviceId,
                deviceName = DeviceInfoHelper.getDeviceName(),
                ownerUid = prefs.currentUser?.uid ?: "local_owner",
                eventType = AppConstants.EVENT_LOCATION_UPDATED,
                title = "Live Location Recorded",
                description = "Coordinates: (${String.format("%.4f", location.latitude)}, ${String.format("%.4f", location.longitude)}), accuracy: ±${location.accuracy.toInt()}m",
                severity = "INFO"
            )
        )
    }

    private fun stopForegroundTracking() {
        if (!isTracking) return
        isTracking = false
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopForegroundTracking()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "com.securox.action.START_LOCATION_TRACKING"
        const val ACTION_STOP = "com.securox.action.STOP_LOCATION_TRACKING"

        fun start(context: Context) {
            val intent = Intent(context, SecuroxLocationService::class.java).apply {
                action = ACTION_START
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, SecuroxLocationService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
