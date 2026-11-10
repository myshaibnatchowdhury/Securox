package com.securox.antitheft.data.model

import com.securox.antitheft.core.constants.AppConstants

data class SecurityCommand(
    val commandId: String = "",
    val ownerUid: String = "",
    val targetDeviceId: String = "",
    val commandType: String = AppConstants.CMD_LOCK_DEVICE,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + AppConstants.COMMAND_EXPIRATION_MS,
    val status: String = AppConstants.STATUS_PENDING, // PENDING, SENDING, DELIVERED, EXECUTED, FAILED, EXPIRED, OFFLINE
    val executionResult: String = "",
    val executedAt: Long? = null,
    val deliveredAt: Long? = null,
    val payload: Map<String, String> = emptyMap()
)
