package com.securox.antitheft.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.data.model.UserAccount

class EncryptedPreferencesManager(context: Context) {

    private val gson = Gson()

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "securox_secure_vault",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // User session
    var currentUser: UserAccount?
        get() {
            val json = prefs.getString(KEY_CURRENT_USER, null) ?: return null
            return runCatching { gson.fromJson(json, UserAccount::class.java) }.getOrNull()
        }
        set(value) {
            prefs.edit().apply {
                if (value != null) {
                    putString(KEY_CURRENT_USER, gson.toJson(value))
                } else {
                    remove(KEY_CURRENT_USER)
                }
                apply()
            }
        }

    // Onboarding
    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

    // Dual-Mode toggle: Cloud vs Local Test Mode
    var isCloudModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_CLOUD_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_CLOUD_MODE, value).apply()

    // Registered Device ID for this physical device
    var registeredDeviceId: String?
        get() = prefs.getString(KEY_REGISTERED_DEVICE_ID, null)
        set(value) = prefs.edit().putString(KEY_REGISTERED_DEVICE_ID, value).apply()

    // Selected Target Device ID in dashboard
    var selectedTargetDeviceId: String?
        get() = prefs.getString(KEY_SELECTED_TARGET_DEVICE_ID, null)
        set(value) = prefs.edit().putString(KEY_SELECTED_TARGET_DEVICE_ID, value).apply()

    // Local Device Cache
    fun saveDevices(devices: List<DeviceInfo>) {
        val json = gson.toJson(devices)
        prefs.edit().putString(KEY_DEVICES_LIST, json).apply()
    }

    fun getDevices(): List<DeviceInfo> {
        val json = prefs.getString(KEY_DEVICES_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<DeviceInfo>>() {}.type
        return runCatching { gson.fromJson<List<DeviceInfo>>(json, type) }.getOrDefault(emptyList())
    }

    // Security Commands Log / Audit Trail
    fun saveCommands(commands: List<SecurityCommand>) {
        val json = gson.toJson(commands)
        prefs.edit().putString(KEY_COMMANDS_LIST, json).apply()
    }

    fun getCommands(): List<SecurityCommand> {
        val json = prefs.getString(KEY_COMMANDS_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<SecurityCommand>>() {}.type
        return runCatching { gson.fromJson<List<SecurityCommand>>(json, type) }.getOrDefault(emptyList())
    }

    fun addCommand(command: SecurityCommand) {
        val current = getCommands().toMutableList()
        val existingIndex = current.indexOfFirst { it.commandId == command.commandId }
        if (existingIndex >= 0) {
            current[existingIndex] = command
        } else {
            current.add(0, command)
        }
        saveCommands(current)
    }

    // Security Events Log
    fun saveEvents(events: List<SecurityEvent>) {
        val json = gson.toJson(events)
        prefs.edit().putString(KEY_EVENTS_LIST, json).apply()
    }

    fun getEvents(): List<SecurityEvent> {
        val json = prefs.getString(KEY_EVENTS_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<SecurityEvent>>() {}.type
        return runCatching { gson.fromJson<List<SecurityEvent>>(json, type) }.getOrDefault(emptyList())
    }

    fun addEvent(event: SecurityEvent) {
        val current = getEvents().toMutableList()
        current.add(0, event)
        saveEvents(current)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_CURRENT_USER = "current_user_account"
        private const val KEY_ONBOARDING_DONE = "onboarding_completed"
        private const val KEY_CLOUD_MODE = "cloud_mode_enabled"
        private const val KEY_REGISTERED_DEVICE_ID = "registered_device_id"
        private const val KEY_SELECTED_TARGET_DEVICE_ID = "selected_target_device_id"
        private const val KEY_DEVICES_LIST = "cached_devices_list"
        private const val KEY_COMMANDS_LIST = "cached_commands_list"
        private const val KEY_EVENTS_LIST = "cached_events_list"
    }
}
