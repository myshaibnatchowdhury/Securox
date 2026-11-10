package com.securox.antitheft.security

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.UserManager

class DeviceOwnerManager(private val context: Context) {

    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val adminComponent = ComponentName(context, SecuroxDeviceAdminReceiver::class.java)

    fun isDeviceOwner(): Boolean {
        return dpm.isDeviceOwnerApp(context.packageName)
    }

    fun isProfileOwner(): Boolean {
        return dpm.isProfileOwnerApp(context.packageName)
    }

    fun canManageEnterprisePolicies(): Boolean {
        return isDeviceOwner()
    }

    fun applyEnterpriseRestrictions(enable: Boolean): Boolean {
        if (!isDeviceOwner()) return false

        return try {
            if (enable) {
                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_FACTORY_RESET)
                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_SAFE_BOOT)
                dpm.addUserRestriction(adminComponent, UserManager.DISALLOW_UNINSTALL_APPS)
            } else {
                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_FACTORY_RESET)
                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_SAFE_BOOT)
                dpm.clearUserRestriction(adminComponent, UserManager.DISALLOW_UNINSTALL_APPS)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isFactoryResetBlocked(): Boolean {
        if (!isDeviceOwner()) return false
        val userRestrictions = dpm.getUserRestrictions(adminComponent)
        return userRestrictions.getBoolean(UserManager.DISALLOW_FACTORY_RESET, false)
    }

    fun isSafeBootBlocked(): Boolean {
        if (!isDeviceOwner()) return false
        val userRestrictions = dpm.getUserRestrictions(adminComponent)
        return userRestrictions.getBoolean(UserManager.DISALLOW_SAFE_BOOT, false)
    }

    fun isUninstallBlocked(): Boolean {
        if (!isDeviceOwner()) return false
        val userRestrictions = dpm.getUserRestrictions(adminComponent)
        return userRestrictions.getBoolean(UserManager.DISALLOW_UNINSTALL_APPS, false)
    }

    fun getAdbProvisioningCommand(): String {
        return "adb shell dpm set-device-owner com.securox.antitheft/.security.SecuroxDeviceAdminReceiver"
    }
}
