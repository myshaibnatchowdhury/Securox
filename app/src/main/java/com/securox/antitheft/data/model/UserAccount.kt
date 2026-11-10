package com.securox.antitheft.data.model

data class UserAccount(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isEmailVerified: Boolean = false,
    val registeredDeviceIds: List<String> = emptyList(),
    val lastLoginAt: Long = System.currentTimeMillis()
)
