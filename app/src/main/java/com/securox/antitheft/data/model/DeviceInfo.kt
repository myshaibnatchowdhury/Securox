package com.securox.antitheft.data.model

data class DeviceInfo(
    val deviceId: String = "",
    val ownerUid: String = "",
    val deviceName: String = "",
    val manufacturer: String = "",
    val model: String = "",
    val androidVersion: String = "",
    val appVersion: String = "1.0.0",
    val registeredAt: Long = System.currentTimeMillis(),
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = true,
    val batteryLevel: Int = 100,
    val isCharging: Boolean = false,
    val lastLatitude: Double? = null,
    val lastLongitude: Double? = null,
    val lastAccuracy: Float? = null,
    val lastLocationTime: Long? = null,
    val isAdminActive: Boolean = false,
    val isDeviceOwner: Boolean = false,
    val protectionStatus: String = "PROTECTED", // PROTECTED, PARTIALLY_PROTECTED, UNPROTECTED
    val fcmToken: String = "",
    val isCurrentDevice: Boolean = false,
    val simState: String = "READY",
    val carrierName: String = "Unknown",
    val simSerialLast4: String = "••••"
)
