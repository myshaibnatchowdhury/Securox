package com.securox.antitheft.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.utils.DeviceInfoHelper
import com.securox.antitheft.core.utils.NetworkMonitor
import com.securox.antitheft.core.utils.NotificationHelper
import com.securox.antitheft.data.local.EncryptedPreferencesManager
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.data.model.TrustedDevice
import com.securox.antitheft.data.model.UserAccount
import com.securox.antitheft.data.remote.SecuroxRepositoryImpl
import com.securox.antitheft.domain.repository.ISecuroxRepository
import com.securox.antitheft.location.LocationTracker
import com.securox.antitheft.location.SecuroxLocationService
import com.securox.antitheft.security.DeviceOwnerManager
import com.securox.antitheft.security.DeviceSecurityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class LockExecutionPhase {
    IDLE, CONFIRMING, SENDING, DELIVERED, EXECUTED, FAILED, OFFLINE
}

data class LockStatusState(
    val phase: LockExecutionPhase = LockExecutionPhase.IDLE,
    val command: SecurityCommand? = null,
    val message: String = ""
)

class SecuroxViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = EncryptedPreferencesManager(application)
    private val repository: ISecuroxRepository = SecuroxRepositoryImpl(application, prefs)
    val securityManager = DeviceSecurityManager(application)
    val ownerManager = DeviceOwnerManager(application)
    private val locationTracker = LocationTracker(application)
    private val networkMonitor = NetworkMonitor(application)

    // Auth & User
    val currentUser: StateFlow<UserAccount?> = repository.currentUser
    val isCloudMode: StateFlow<Boolean> = repository.isCloudMode

    // Devices & Multi-device control
    val devices: StateFlow<List<DeviceInfo>> = repository.devices
    private val _selectedDeviceId = MutableStateFlow<String?>(prefs.selectedTargetDeviceId)
    val selectedDeviceId: StateFlow<String?> = _selectedDeviceId.asStateFlow()

    val selectedTargetDevice: StateFlow<DeviceInfo?> = combine(devices, selectedDeviceId) { devList, selId ->
        devList.find { it.deviceId == selId } ?: devList.firstOrNull { !it.isCurrentDevice } ?: devList.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Security Commands & Events
    val commands: StateFlow<List<SecurityCommand>> = repository.commands
    val events: StateFlow<List<SecurityEvent>> = repository.events
    val trustedDevices: StateFlow<List<TrustedDevice>> = repository.trustedDevices

    // Live Network Connectivity
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Emergency Lock Interactive State
    private val _lockStatus = MutableStateFlow(LockStatusState())
    val lockStatus: StateFlow<LockStatusState> = _lockStatus.asStateFlow()

    // Hardware Enforcing Status
    private val _isAdminActive = MutableStateFlow(securityManager.isAdminActive())
    val isAdminActive: StateFlow<Boolean> = _isAdminActive.asStateFlow()

    private val _isDeviceOwner = MutableStateFlow(ownerManager.isDeviceOwner())
    val isDeviceOwner: StateFlow<Boolean> = _isDeviceOwner.asStateFlow()

    private val _isLocationTrackingRunning = MutableStateFlow(false)
    val isLocationTrackingRunning: StateFlow<Boolean> = _isLocationTrackingRunning.asStateFlow()

    // Feedback message (snackbar / toast)
    private val _userFeedback = MutableStateFlow<String?>(null)
    val userFeedback: StateFlow<String?> = _userFeedback.asStateFlow()

    init {
        NotificationHelper.createNotificationChannels(application)
        refreshHardwareStatus()
    }

    fun clearFeedback() {
        _userFeedback.value = null
    }

    fun selectTargetDevice(deviceId: String) {
        _selectedDeviceId.value = deviceId
        prefs.selectedTargetDeviceId = deviceId
    }

    fun refreshHardwareStatus() {
        val admin = securityManager.isAdminActive()
        val owner = ownerManager.isDeviceOwner()
        _isAdminActive.value = admin
        _isDeviceOwner.value = owner

        val currentId = DeviceInfoHelper.getDeviceId(getApplication())
        val battery = DeviceInfoHelper.getBatteryLevel(getApplication())
        val charging = DeviceInfoHelper.isCharging(getApplication())

        viewModelScope.launch {
            repository.updateDeviceStatus(
                deviceId = currentId,
                battery = battery,
                charging = charging,
                online = networkMonitor.checkCurrentConnectivity(),
                admin = admin,
                owner = owner
            )
        }
    }

    /**
     * Executes or dispatches the REMOTE EMERGENCY LOCK
     */
    fun startEmergencyLockSequence() {
        _lockStatus.value = LockStatusState(phase = LockExecutionPhase.CONFIRMING)
    }

    fun cancelEmergencyLock() {
        _lockStatus.value = LockStatusState(phase = LockExecutionPhase.IDLE)
    }

    fun confirmAndSendEmergencyLock(targetDevice: DeviceInfo) {
        val user = currentUser.value
        val commandId = "cmd_${UUID.randomUUID().toString().take(8)}"
        val command = SecurityCommand(
            commandId = commandId,
            ownerUid = user?.uid ?: "local_owner",
            targetDeviceId = targetDevice.deviceId,
            commandType = AppConstants.CMD_LOCK_DEVICE,
            createdAt = System.currentTimeMillis(),
            status = AppConstants.STATUS_SENDING
        )

        _lockStatus.value = LockStatusState(
            phase = LockExecutionPhase.SENDING,
            command = command,
            message = "Authenticating owner signature and transmitting command..."
        )

        viewModelScope.launch {
            // Check if user is locking this local physical device directly
            if (targetDevice.isCurrentDevice) {
                if (securityManager.isAdminActive()) {
                    securityManager.lockDevice()
                    val executedCmd = command.copy(
                        status = AppConstants.STATUS_EXECUTED,
                        executionResult = "Local device screen immediately locked via DevicePolicyManager lockNow()",
                        executedAt = System.currentTimeMillis()
                    )
                    repository.sendSecurityCommand(executedCmd)
                    _lockStatus.value = LockStatusState(
                        phase = LockExecutionPhase.EXECUTED,
                        command = executedCmd,
                        message = executedCmd.executionResult
                    )
                } else {
                    val failedCmd = command.copy(
                        status = AppConstants.STATUS_FAILED,
                        executionResult = "Device Administrator privileges are not active on this device"
                    )
                    _lockStatus.value = LockStatusState(
                        phase = LockExecutionPhase.FAILED,
                        command = failedCmd,
                        message = failedCmd.executionResult
                    )
                }
                return@launch
            }

            // Remote device locking
            val result = repository.sendSecurityCommand(command)
            result.onSuccess { executedCmd ->
                when (executedCmd.status) {
                    AppConstants.STATUS_EXECUTED -> {
                        _lockStatus.value = LockStatusState(
                            phase = LockExecutionPhase.EXECUTED,
                            command = executedCmd,
                            message = executedCmd.executionResult
                        )
                        _userFeedback.value = "Emergency Lock Confirmed on ${targetDevice.deviceName}"
                    }
                    AppConstants.STATUS_OFFLINE -> {
                        _lockStatus.value = LockStatusState(
                            phase = LockExecutionPhase.OFFLINE,
                            command = executedCmd,
                            message = "Target device is OFFLINE. Lock command queued for transmission upon reconnect."
                        )
                    }
                    else -> {
                        _lockStatus.value = LockStatusState(
                            phase = LockExecutionPhase.DELIVERED,
                            command = executedCmd,
                            message = "Command delivered to device. Awaiting hardware lock confirmation..."
                        )
                    }
                }
            }.onFailure { err ->
                _lockStatus.value = LockStatusState(
                    phase = LockExecutionPhase.FAILED,
                    command = command,
                    message = err.message ?: "Failed to transmit lock command"
                )
            }
        }
    }

    /**
     * Locate device
     */
    fun sendAdminAction(targetDevice: DeviceInfo, commandType: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val cmd = SecurityCommand(
                    commandId = "cmd_" + java.util.UUID.randomUUID().toString().take(8),
                    ownerUid = currentUser.value?.uid ?: "local_owner",
                    targetDeviceId = targetDevice.deviceId,
                    commandType = commandType,
                    createdAt = System.currentTimeMillis(),
                    status = AppConstants.STATUS_SENDING
                )
                val res = repository.sendSecurityCommand(cmd)
                res.onSuccess {
                    val msg = "Admin action '" + commandType + "' sent to " + targetDevice.deviceName + " [Status: " + it.status + "]"
                    _userFeedback.value = msg
                    onResult(true, msg)
                }.onFailure { err ->
                    val msg = "Admin action failed: " + err.message
                    _userFeedback.value = msg
                    onResult(false, msg)
                }
            } catch (e: Exception) {
                val msg = "Failed: " + e.message
                _userFeedback.value = msg
                onResult(false, msg)
            }
        }
    }

    fun triggerLocateDevice(targetDevice: DeviceInfo) {
        viewModelScope.launch {
            if (targetDevice.isCurrentDevice) {
                val loc = locationTracker.getCurrentLocation()
                if (loc != null) {
                    repository.updateDeviceLocation(
                        deviceId = targetDevice.deviceId,
                        lat = loc.latitude,
                        lng = loc.longitude,
                        accuracy = loc.accuracy
                    )
                    _userFeedback.value = "GPS Fix: ${String.format("%.4f", loc.latitude)}, ${String.format("%.4f", loc.longitude)} (±${loc.accuracy.toInt()}m)"
                } else {
                    _userFeedback.value = "Unable to obtain instant GPS fix. Ensure location is enabled."
                }
            } else {
                val cmd = SecurityCommand(
                    commandId = "cmd_${UUID.randomUUID().toString().take(8)}",
                    ownerUid = currentUser.value?.uid ?: "local_owner",
                    targetDeviceId = targetDevice.deviceId,
                    commandType = AppConstants.CMD_LOCATE_DEVICE
                )
                repository.sendSecurityCommand(cmd)
                _userFeedback.value = "Locate command dispatched to ${targetDevice.deviceName}"
            }
        }
    }

    /**
     * Start/Stop persistent location tracking
     */
    fun toggleForegroundLocationTracking(start: Boolean) {
        val app = getApplication<Application>()
        if (start) {
            SecuroxLocationService.start(app)
            _isLocationTrackingRunning.value = true
            _userFeedback.value = "Persistent background GPS tracking active"
        } else {
            SecuroxLocationService.stop(app)
            _isLocationTrackingRunning.value = false
            _userFeedback.value = "Location service stopped"
        }
    }

    /**
     * Enterprise Device Owner Policies
     */
    fun toggleEnterpriseRestrictions(enable: Boolean) {
        if (!ownerManager.isDeviceOwner()) {
            _userFeedback.value = "Requires Android Enterprise Device Owner mode"
            return
        }
        val success = ownerManager.applyEnterpriseRestrictions(enable)
        if (success) {
            _userFeedback.value = if (enable) "Factory Reset & Safe Boot restrictions enforced" else "Restrictions cleared"
            viewModelScope.launch {
                repository.logSecurityEvent(
                    SecurityEvent(
                        id = "evt_${UUID.randomUUID().toString().take(8)}",
                        deviceId = DeviceInfoHelper.getDeviceId(getApplication()),
                        deviceName = DeviceInfoHelper.getDeviceName(),
                        ownerUid = currentUser.value?.uid ?: "local_owner",
                        eventType = AppConstants.EVENT_DEVICE_OWNER_CHANGED,
                        title = "Enterprise Restrictions Updated",
                        description = if (enable) "Factory reset & safe boot blocked." else "Enterprise restrictions disabled.",
                        severity = "HIGH"
                    )
                )
            }
        } else {
            _userFeedback.value = "Failed to apply enterprise restrictions"
        }
    }

    /**
     * Auth Methods
     */
    fun authenticate(email: String, pass: String, isSignUp: Boolean, name: String = "", onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = if (isSignUp) {
                repository.signUp(email, pass, name)
            } else {
                repository.signIn(email, pass)
            }
            result.onSuccess {
                prefs.isOnboardingCompleted = true
                onResult(true, null)
            }.onFailure {
                onResult(false, it.message ?: "Authentication failed")
            }
        }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.signOut()
            onComplete()
        }
    }

    fun resetPassword(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repository.resetPassword(email)
            onResult(true, "Password reset instructions sent to $email")
        }
    }

    fun registerNewDevice(name: String, model: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val newDevice = DeviceInfo(
                deviceId = "dev_${UUID.randomUUID().toString().take(8)}",
                ownerUid = currentUser.value?.uid ?: "local_owner",
                deviceName = name.ifBlank { "Personal Device" },
                manufacturer = model.split(" ").firstOrNull() ?: "Android",
                model = model.ifBlank { "Smart Phone" },
                androidVersion = "Android 14 (API 34)",
                registeredAt = System.currentTimeMillis(),
                lastSeen = System.currentTimeMillis(),
                isOnline = true,
                batteryLevel = 100,
                isCharging = false,
                lastLatitude = 37.7749,
                lastLongitude = -122.4194,
                isAdminActive = false,
                isDeviceOwner = false,
                protectionStatus = "PARTIALLY_PROTECTED",
                isCurrentDevice = false
            )
            repository.registerDevice(newDevice)
            selectTargetDevice(newDevice.deviceId)
            _userFeedback.value = "Device '${newDevice.deviceName}' successfully enrolled!"
            onComplete()
        }
    }

    fun removeDevice(deviceId: String) {
        viewModelScope.launch {
            repository.removeDevice(deviceId)
            if (_selectedDeviceId.value == deviceId) {
                _selectedDeviceId.value = devices.value.firstOrNull { it.deviceId != deviceId }?.deviceId
            }
            _userFeedback.value = "Device removed from account"
        }
    }

    fun toggleCloudMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setCloudMode(enabled)
            _userFeedback.value = if (enabled) "Switched to Firebase Cloud Sync" else "Switched to Local Vault Test Mode"
        }
    }

    fun addTrustedContactDevice(name: String, model: String, email: String) {
        viewModelScope.launch {
            val trusted = TrustedDevice(
                id = "trust_${UUID.randomUUID().toString().take(8)}",
                ownerUid = currentUser.value?.uid ?: "local_owner",
                deviceName = name,
                model = model,
                ownerEmail = email
            )
            repository.addTrustedDevice(trusted)
            _userFeedback.value = "Trusted device '$name' added"
        }
    }

    fun removeTrustedContactDevice(id: String) {
        viewModelScope.launch {
            repository.removeTrustedDevice(id)
            _userFeedback.value = "Trusted device removed"
        }
    }
}
