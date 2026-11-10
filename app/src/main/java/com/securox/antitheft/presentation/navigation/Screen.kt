package com.securox.antitheft.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    // Core Bottom Nav Tabs
    object Home : Screen("home")
    object AdminControlDashboard : Screen("admin_control_dashboard")
    object MyDevices : Screen("my_devices")
    object SecurityCenter : Screen("security_center")
    object SecurityAlerts : Screen("security_alerts")
    object AccountSettings : Screen("account_settings")

    // Dedicated Feature Screens
    object RegisterDevice : Screen("register_device")
    object DeviceDetails : Screen("device_details")
    object EmergencyLock : Screen("emergency_lock")
    object LocationTracking : Screen("location_tracking")
    object DeviceAdminSetup : Screen("device_admin_setup")
    object DeviceOwnerSetup : Screen("device_owner_setup")
    object AntiShutdown : Screen("anti_shutdown")
    object FactoryResetProtection : Screen("factory_reset_protection")
    object SimSecurity : Screen("sim_security")
    object CommandHistory : Screen("command_history")
    object TrustedDevices : Screen("trusted_devices")
    object PrivacyPolicy : Screen("privacy_policy")
    object About : Screen("about")
}
