package com.securox.antitheft.core.constants

object AppConstants {
    const val APP_NAME = "SECUROX"
    const val APP_FULL_NAME = "SECUROX - Advanced Anti-Theft & Device Security"
    const val APP_VERSION = "1.0.0"
    const val PACKAGE_NAME = "com.securox.antitheft"
    const val TAGLINE_PRIMARY = "Your Phone. Our Protection."
    const val TAGLINE_SECONDARY = "Secure Today. Safe Tomorrow."

    // Command Types
    const val CMD_LOCK_DEVICE = "LOCK_DEVICE"
    const val CMD_LOCATE_DEVICE = "LOCATE_DEVICE"
    const val CMD_SOUND_ALARM = "SOUND_ALARM"
    const val CMD_ENTER_LOCKDOWN = "ENTER_LOCKDOWN"
    const val CMD_WIPE_DATA = "WIPE_DATA"
    const val CMD_REFRESH_DEVICE_STATUS = "REFRESH_DEVICE_STATUS"
    const val CMD_CHECK_SECURITY_STATUS = "CHECK_SECURITY_STATUS"
    const val CMD_ENABLE_RESTRICTIONS = "ENABLE_SUPPORTED_RESTRICTIONS"
    const val CMD_DISABLE_RESTRICTIONS = "DISABLE_SUPPORTED_RESTRICTIONS"

    // Command Statuses
    const val STATUS_PENDING = "PENDING"
    const val STATUS_SENDING = "SENDING"
    const val STATUS_DELIVERED = "DELIVERED"
    const val STATUS_EXECUTED = "EXECUTED"
    const val STATUS_FAILED = "FAILED"
    const val STATUS_EXPIRED = "EXPIRED"
    const val STATUS_OFFLINE = "OFFLINE"

    // Security Event Types
    const val EVENT_LOCK_REQUESTED = "EMERGENCY_LOCK_REQUESTED"
    const val EVENT_LOCK_COMPLETED = "EMERGENCY_LOCK_COMPLETED"
    const val EVENT_DEVICE_OFFLINE = "DEVICE_OFFLINE"
    const val EVENT_DEVICE_ONLINE = "DEVICE_ONLINE"
    const val EVENT_UNEXPECTED_REBOOT = "UNEXPECTED_REBOOT"
    const val EVENT_SIM_STATE_CHANGED = "SIM_STATE_CHANGED"
    const val EVENT_LOCATION_UPDATED = "LOCATION_UPDATED"
    const val EVENT_ADMIN_DISABLED = "ADMIN_DISABLED"
    const val EVENT_ADMIN_ENABLED = "ADMIN_ENABLED"
    const val EVENT_DEVICE_OWNER_CHANGED = "DEVICE_OWNER_POLICY_CHANGED"
    const val EVENT_PERMISSION_REVOKED = "LOCATION_PERMISSION_REVOKED"
    const val EVENT_SERVICE_STOPPED = "SECURITY_SERVICE_STOPPED"
    const val EVENT_BATTERY_LOW = "BATTERY_CRITICALLY_LOW"
    const val EVENT_POWER_DISCONNECTED = "POWER_DISCONNECTED"

    // Notification Channel IDs
    const val CHANNEL_SECURITY_ALERTS = "securox_security_alerts"
    const val CHANNEL_LOCATION_SERVICE = "securox_location_service"
    const val LOCATION_NOTIFICATION_ID = 1001

    // Expiration
    const val COMMAND_EXPIRATION_MS = 5 * 60 * 1000L // 5 minutes
    const val HEARTBEAT_INTERVAL_MINUTES = 15L
}
