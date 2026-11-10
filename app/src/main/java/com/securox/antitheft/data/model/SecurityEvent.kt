package com.securox.antitheft.data.model

data class SecurityEvent(
    val id: String = "",
    val deviceId: String = "",
    val deviceName: String = "",
    val ownerUid: String = "",
    val eventType: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val severity: String = "INFO" // INFO, WARNING, CRITICAL
)
