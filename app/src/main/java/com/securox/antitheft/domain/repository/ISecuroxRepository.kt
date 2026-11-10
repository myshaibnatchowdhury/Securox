package com.securox.antitheft.domain.repository

import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.data.model.LocationRecord
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.data.model.TrustedDevice
import com.securox.antitheft.data.model.UserAccount
import kotlinx.coroutines.flow.StateFlow

interface ISecuroxRepository {
    val currentUser: StateFlow<UserAccount?>
    val devices: StateFlow<List<DeviceInfo>>
    val commands: StateFlow<List<SecurityCommand>>
    val events: StateFlow<List<SecurityEvent>>
    val trustedDevices: StateFlow<List<TrustedDevice>>
    val isCloudMode: StateFlow<Boolean>

    suspend fun signUp(email: String, pass: String, name: String): Result<UserAccount>
    suspend fun signIn(email: String, pass: String): Result<UserAccount>
    suspend fun signOut()
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>

    suspend fun registerDevice(device: DeviceInfo): Result<DeviceInfo>
    suspend fun updateDeviceStatus(
        deviceId: String,
        battery: Int,
        charging: Boolean,
        online: Boolean,
        admin: Boolean,
        owner: Boolean
    ): Result<Unit>
    suspend fun removeDevice(deviceId: String): Result<Unit>

    suspend fun sendSecurityCommand(command: SecurityCommand): Result<SecurityCommand>
    suspend fun updateCommandStatus(
        commandId: String,
        status: String,
        result: String
    ): Result<Unit>

    suspend fun updateDeviceLocation(
        deviceId: String,
        lat: Double,
        lng: Double,
        accuracy: Float
    ): Result<Unit>

    suspend fun logSecurityEvent(event: SecurityEvent): Result<Unit>
    suspend fun setCloudMode(enabled: Boolean)
    suspend fun addTrustedDevice(trusted: TrustedDevice): Result<Unit>
    suspend fun removeTrustedDevice(trustedId: String): Result<Unit>
}
