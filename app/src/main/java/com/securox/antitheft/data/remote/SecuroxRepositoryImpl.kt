package com.securox.antitheft.data.remote

import android.content.Context
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.data.model.TrustedDevice
import com.securox.antitheft.data.model.UserAccount
import com.securox.antitheft.domain.repository.ISecuroxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SecuroxRepositoryImpl(
    private val context: Context,
    private val prefs: EncryptedPreferencesManager
) : ISecuroxRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<UserAccount?>(prefs.currentUser)
    override val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    private val _devices = MutableStateFlow<List<DeviceInfo>>(emptyList())
    override val devices: StateFlow<List<DeviceInfo>> = _devices.asStateFlow()

    private val _commands = MutableStateFlow<List<SecurityCommand>>(emptyList())
    override val commands: StateFlow<List<SecurityCommand>> = _commands.asStateFlow()

    private val _events = MutableStateFlow<List<SecurityEvent>>(emptyList())
    override val events: StateFlow<List<SecurityEvent>> = _events.asStateFlow()

    private val _trustedDevices = MutableStateFlow<List<TrustedDevice>>(emptyList())
    override val trustedDevices: StateFlow<List<TrustedDevice>> = _trustedDevices.asStateFlow()

    private val _isCloudMode = MutableStateFlow(prefs.isCloudModeEnabled)
    override val isCloudMode: StateFlow<Boolean> = _isCloudMode.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val cachedDevices = prefs.getDevices()
        if (cachedDevices.isEmpty()) {
            val initial = createDefaultMultiDevices()
            _devices.value = initial
            prefs.saveDevices(initial)
        } else {
            _devices.value = cachedDevices
        }

        val cachedCommands = prefs.getCommands()
        if (cachedCommands.isEmpty()) {
            val sampleCommands = createSampleCommands()
            _commands.value = sampleCommands
            prefs.saveCommands(sampleCommands)
        } else {
            _commands.value = cachedCommands
        }

        val cachedEvents = prefs.getEvents()
        if (cachedEvents.isEmpty()) {
            val sampleEvents = createSampleEvents()
            _events.value = sampleEvents
            prefs.saveEvents(sampleEvents)
        } else {
            _events.value = cachedEvents
        }

        _trustedDevices.value = listOf(
            TrustedDevice(
                id = "trust_01",
                ownerUid = _currentUser.value?.uid ?: "uid_default",
                deviceName = "Office Backup Terminal",
                model = "Pixel Tablet",
                ownerEmail = _currentUser.value?.email ?: "owner@securox.io",
                isPrimary = false
            ),
            TrustedDevice(
                id = "trust_02",
                ownerUid = _currentUser.value?.uid ?: "uid_default",
                deviceName = "Personal Laptop Shield",
                model = "Enterprise Linux",
                ownerEmail = _currentUser.value?.email ?: "owner@securox.io",
                isPrimary = true
            )
        )
    }

    private fun createDefaultMultiDevices(): List<DeviceInfo> {
        val currentDeviceId = DeviceInfoHelper.getDeviceId(context)
        val currentDeviceName = DeviceInfoHelper.getDeviceName()

        val currentDevice = DeviceInfo(
            deviceId = currentDeviceId,
            ownerUid = _currentUser.value?.uid ?: "uid_owner_demo",
            deviceName = "$currentDeviceName (This Phone)",
            manufacturer = DeviceInfoHelper.getManufacturer(),
            model = DeviceInfoHelper.getModel(),
            androidVersion = DeviceInfoHelper.getAndroidVersion(),
            registeredAt = System.currentTimeMillis() - (86400000L * 5),
            lastSeen = System.currentTimeMillis(),
            isOnline = true,
            batteryLevel = DeviceInfoHelper.getBatteryLevel(context),
            isCharging = DeviceInfoHelper.isCharging(context),
            lastLatitude = 37.7749,
            lastLongitude = -122.4194,
            lastAccuracy = 4.2f,
            lastLocationTime = System.currentTimeMillis(),
            isAdminActive = false,
            isDeviceOwner = false,
            protectionStatus = "PARTIALLY_PROTECTED",
            isCurrentDevice = true,
            simState = "READY",
            carrierName = "T-Mobile / LTE",
            simSerialLast4 = "9821"
        )

        val phoneAProtected = DeviceInfo(
            deviceId = "dev_protected_a",
            ownerUid = _currentUser.value?.uid ?: "uid_owner_demo",
            deviceName = "Phone A — Personal Daily Driver",
            manufacturer = "Samsung",
            model = "Galaxy S24 Ultra",
            androidVersion = "Android 14 (API 34)",
            registeredAt = System.currentTimeMillis() - (86400000L * 14),
            lastSeen = System.currentTimeMillis() - 120000L,
            isOnline = true,
            batteryLevel = 87,
            isCharging = false,
            lastLatitude = 37.7833,
            lastLongitude = -122.4167,
            lastAccuracy = 3.5f,
            lastLocationTime = System.currentTimeMillis() - 120000L,
            isAdminActive = true,
            isDeviceOwner = true,
            protectionStatus = "PROTECTED",
            isCurrentDevice = false,
            simState = "READY",
            carrierName = "Verizon 5G",
            simSerialLast4 = "4312"
        )

        val phoneCLost = DeviceInfo(
            deviceId = "dev_field_c",
            ownerUid = _currentUser.value?.uid ?: "uid_owner_demo",
            deviceName = "Phone C — Field Work Device",
            manufacturer = "Google",
            model = "Pixel 8 Pro",
            androidVersion = "Android 14 (API 34)",
            registeredAt = System.currentTimeMillis() - (86400000L * 30),
            lastSeen = System.currentTimeMillis() - (3600000L * 3),
            isOnline = false,
            batteryLevel = 19,
            isCharging = false,
            lastLatitude = 37.7690,
            lastLongitude = -122.4467,
            lastAccuracy = 12.0f,
            lastLocationTime = System.currentTimeMillis() - (3600000L * 3),
            isAdminActive = true,
            isDeviceOwner = false,
            protectionStatus = "PARTIALLY_PROTECTED",
            isCurrentDevice = false,
            simState = "REMOVED",
            carrierName = "AT&T",
            simSerialLast4 = "1098"
        )

        return listOf(currentDevice, phoneAProtected, phoneCLost)
    }

    private fun createSampleCommands(): List<SecurityCommand> {
        val now = System.currentTimeMillis()
        return listOf(
            SecurityCommand(
                commandId = "cmd_${UUID.randomUUID().toString().take(8)}",
                ownerUid = "uid_owner_demo",
                targetDeviceId = "dev_protected_a",
                commandType = AppConstants.CMD_LOCK_DEVICE,
                createdAt = now - 7200000L,
                status = AppConstants.STATUS_EXECUTED,
                executionResult = "Device successfully locked via DevicePolicyManager lockNow()",
                executedAt = now - 7198000L
            ),
            SecurityCommand(
                commandId = "cmd_${UUID.randomUUID().toString().take(8)}",
                ownerUid = "uid_owner_demo",
                targetDeviceId = "dev_protected_a",
                commandType = AppConstants.CMD_LOCATE_DEVICE,
                createdAt = now - 14400000L,
                status = AppConstants.STATUS_EXECUTED,
                executionResult = "High accuracy GPS coordinate captured (lat: 37.7833, lng: -122.4167)",
                executedAt = now - 14397000L
            )
        )
    }

    private fun createSampleEvents(): List<SecurityEvent> {
        val now = System.currentTimeMillis()
        return listOf(
            SecurityEvent(
                id = "evt_01",
                deviceId = "dev_field_c",
                deviceName = "Phone C — Field Work Device",
                ownerUid = "uid_owner_demo",
                eventType = AppConstants.EVENT_SIM_STATE_CHANGED,
                title = "SIM Card Removed",
                description = "Hardware SIM card removal detected while device screen was locked.",
                timestamp = now - 10800000L,
                severity = "CRITICAL"
            ),
            SecurityEvent(
                id = "evt_02",
                deviceId = "dev_protected_a",
                deviceName = "Phone A — Personal Daily Driver",
                ownerUid = "uid_owner_demo",
                eventType = AppConstants.EVENT_LOCK_COMPLETED,
                title = "Remote Emergency Lock Confirmed",
                description = "Device locked immediately upon receiving owner remote command.",
                timestamp = now - 7198000L,
                severity = "HIGH"
            ),
            SecurityEvent(
                id = "evt_03",
                deviceId = DeviceInfoHelper.getDeviceId(context),
                deviceName = DeviceInfoHelper.getDeviceName(),
                ownerUid = "uid_owner_demo",
                eventType = AppConstants.EVENT_DEVICE_ONLINE,
                title = "Device Shield Initialized",
                description = "SECUROX Security Engine online and protecting device hardware.",
                timestamp = now - 3600000L,
                severity = "INFO"
            )
        )
    }

    override suspend fun signUp(email: String, pass: String, name: String): Result<UserAccount> {
        delay(400) // Realistic secure auth hashing simulation
        val user = UserAccount(
            uid = "usr_${UUID.randomUUID().toString().take(12)}",
            email = email,
            displayName = name.ifBlank { email.substringBefore("@") },
            createdAt = System.currentTimeMillis(),
            isEmailVerified = true
        )
        prefs.currentUser = user
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signIn(email: String, pass: String): Result<UserAccount> {
        delay(400)
        val user = prefs.currentUser ?: UserAccount(
            uid = "usr_${UUID.randomUUID().toString().take(12)}",
            email = email,
            displayName = email.substringBefore("@"),
            createdAt = System.currentTimeMillis()
        )
        prefs.currentUser = user
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signOut() {
        prefs.currentUser = null
        _currentUser.value = null
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit> {
        prefs.clearAll()
        _currentUser.value = null
        _devices.value = emptyList()
        _commands.value = emptyList()
        _events.value = emptyList()
        return Result.success(Unit)
    }

    override suspend fun registerDevice(device: DeviceInfo): Result<DeviceInfo> {
        val current = _devices.value.toMutableList()
        val index = current.indexOfFirst { it.deviceId == device.deviceId }
        if (index >= 0) {
            current[index] = device
        } else {
            current.add(device)
        }
        _devices.value = current
        prefs.saveDevices(current)
        return Result.success(device)
    }

    override suspend fun updateDeviceStatus(
        deviceId: String,
        battery: Int,
        charging: Boolean,
        online: Boolean,
        admin: Boolean,
        owner: Boolean
    ): Result<Unit> {
        val current = _devices.value.toMutableList()
        val index = current.indexOfFirst { it.deviceId == deviceId }
        if (index >= 0) {
            val dev = current[index]
            val status = when {
                admin && owner -> "PROTECTED"
                admin || owner -> "PARTIALLY_PROTECTED"
                else -> "UNPROTECTED"
            }
            current[index] = dev.copy(
                batteryLevel = battery,
                isCharging = charging,
                isOnline = online,
                isAdminActive = admin,
                isDeviceOwner = owner,
                protectionStatus = status,
                lastSeen = System.currentTimeMillis()
            )
            _devices.value = current
            prefs.saveDevices(current)
        }
        return Result.success(Unit)
    }

    override suspend fun removeDevice(deviceId: String): Result<Unit> {
        val current = _devices.value.toMutableList()
        current.removeAll { it.deviceId == deviceId }
        _devices.value = current
        prefs.saveDevices(current)
        return Result.success(Unit)
    }

    override suspend fun sendSecurityCommand(command: SecurityCommand): Result<SecurityCommand> {
        val currentDevices = _devices.value
        val targetDevice = currentDevices.find { it.deviceId == command.targetDeviceId }

        // If target device is not registered
        if (targetDevice == null) {
            val failedCmd = command.copy(
                status = AppConstants.STATUS_FAILED,
                executionResult = "Target device not found in account inventory"
            )
            prefs.addCommand(failedCmd)
            _commands.value = prefs.getCommands()
            return Result.failure(IllegalStateException(failedCmd.executionResult))
        }

        // If target device is offline
        if (!targetDevice.isOnline) {
            val pendingCmd = command.copy(
                status = AppConstants.STATUS_OFFLINE,
                executionResult = "Target device is currently offline. Command queued."
            )
            prefs.addCommand(pendingCmd)
            _commands.value = prefs.getCommands()

            logSecurityEvent(
                SecurityEvent(
                    id = "evt_${UUID.randomUUID().toString().take(8)}",
                    deviceId = targetDevice.deviceId,
                    deviceName = targetDevice.deviceName,
                    ownerUid = command.ownerUid,
                    eventType = AppConstants.EVENT_DEVICE_OFFLINE,
                    title = "Target Offline for Command",
                    description = "Command ${command.commandType} queued because ${targetDevice.deviceName} is offline.",
                    severity = "WARNING"
                )
            )
            return Result.success(pendingCmd)
        }

        // Target device is ONLINE: Begin authenticated transmission pipeline
        // 1. Initial State: SENDING
        val sendingCmd = command.copy(status = AppConstants.STATUS_SENDING)
        prefs.addCommand(sendingCmd)
        _commands.value = prefs.getCommands()

        // Simulate network hop to FCM & target device
        delay(600)

        // 2. State: DELIVERED
        val deliveredCmd = sendingCmd.copy(
            status = AppConstants.STATUS_DELIVERED,
            deliveredAt = System.currentTimeMillis()
        )
        prefs.addCommand(deliveredCmd)
        _commands.value = prefs.getCommands()

        delay(400)

        // 3. State: EXECUTED on target device
        val resultMessage = when (command.commandType) {
            AppConstants.CMD_LOCK_DEVICE -> "Device locked immediately via DevicePolicyManager lockNow()"
            AppConstants.CMD_LOCATE_DEVICE -> "GPS coordinates captured and synced (lat: ${targetDevice.lastLatitude ?: 37.7749}, lng: ${targetDevice.lastLongitude ?: -122.4194})"
            AppConstants.CMD_REFRESH_DEVICE_STATUS -> "Device telemetry refreshed: Battery ${targetDevice.batteryLevel}%, Online: true"
            AppConstants.CMD_CHECK_SECURITY_STATUS -> "Security audit complete: Admin=${targetDevice.isAdminActive}, Enterprise Owner=${targetDevice.isDeviceOwner}"
            AppConstants.CMD_ENABLE_RESTRICTIONS -> "Factory reset and Safe Boot restrictions enforced"
            AppConstants.CMD_DISABLE_RESTRICTIONS -> "Device policy restrictions relaxed"
            else -> "Command completed successfully"
        }

        val executedCmd = deliveredCmd.copy(
            status = AppConstants.STATUS_EXECUTED,
            executionResult = resultMessage,
            executedAt = System.currentTimeMillis()
        )
        prefs.addCommand(executedCmd)
        _commands.value = prefs.getCommands()

        // Log completion security event
        logSecurityEvent(
            SecurityEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                deviceId = targetDevice.deviceId,
                deviceName = targetDevice.deviceName,
                ownerUid = command.ownerUid,
                eventType = if (command.commandType == AppConstants.CMD_LOCK_DEVICE) AppConstants.EVENT_LOCK_COMPLETED else "COMMAND_EXECUTED",
                title = "Command Executed: ${command.commandType}",
                description = "${targetDevice.deviceName}: $resultMessage",
                severity = if (command.commandType == AppConstants.CMD_LOCK_DEVICE) "CRITICAL" else "INFO"
            )
        )

        return Result.success(executedCmd)
    }

    override suspend fun updateCommandStatus(
        commandId: String,
        status: String,
        result: String
    ): Result<Unit> {
        val cmds = prefs.getCommands().toMutableList()
        val index = cmds.indexOfFirst { it.commandId == commandId }
        if (index >= 0) {
            cmds[index] = cmds[index].copy(
                status = status,
                executionResult = result,
                executedAt = System.currentTimeMillis()
            )
            prefs.saveCommands(cmds)
            _commands.value = cmds
        }
        return Result.success(Unit)
    }

    override suspend fun updateDeviceLocation(
        deviceId: String,
        lat: Double,
        lng: Double,
        accuracy: Float
    ): Result<Unit> {
        val current = _devices.value.toMutableList()
        val index = current.indexOfFirst { it.deviceId == deviceId }
        if (index >= 0) {
            val dev = current[index]
            current[index] = dev.copy(
                lastLatitude = lat,
                lastLongitude = lng,
                lastAccuracy = accuracy,
                lastLocationTime = System.currentTimeMillis()
            )
            _devices.value = current
            prefs.saveDevices(current)
        }
        return Result.success(Unit)
    }

    override suspend fun logSecurityEvent(event: SecurityEvent): Result<Unit> {
        prefs.addEvent(event)
        _events.value = prefs.getEvents()
        return Result.success(Unit)
    }

    override suspend fun setCloudMode(enabled: Boolean) {
        prefs.isCloudModeEnabled = enabled
        _isCloudMode.value = enabled
    }

    override suspend fun addTrustedDevice(trusted: TrustedDevice): Result<Unit> {
        val current = _trustedDevices.value.toMutableList()
        current.add(trusted)
        _trustedDevices.value = current
        return Result.success(Unit)
    }

    override suspend fun removeTrustedDevice(trustedId: String): Result<Unit> {
        val current = _trustedDevices.value.toMutableList()
        current.removeAll { it.id == trustedId }
        _trustedDevices.value = current
        return Result.success(Unit)
    }
}
