package com.securox.antitheft.data.model

data class LocationRecord(
    val id: String = "",
    val deviceId: String = "",
    val ownerUid: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0.0f,
    val timestamp: Long = System.currentTimeMillis(),
    val provider: String = "fused"
)

data class TrustedDevice(
    val id: String = "",
    val ownerUid: String = "",
    val deviceName: String = "",
    val model: String = "",
    val ownerEmail: String = "",
    val addedAt: Long = System.currentTimeMillis(),
    val isPrimary: Boolean = false
)
