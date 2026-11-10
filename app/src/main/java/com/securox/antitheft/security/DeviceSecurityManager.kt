package com.securox.antitheft.security

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.securox.antitheft.R

class DeviceSecurityManager(private val context: Context) {

    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val adminComponent = ComponentName(context, SecuroxDeviceAdminReceiver::class.java)

    fun isAdminActive(): Boolean {
        return dpm.isAdminActive(adminComponent)
    }

    fun createAdminActivationIntent(): Intent {
        return Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                context.getString(R.string.device_admin_description)
            )
        }
    }

    /**
     * Executes immediate screen lock using DevicePolicyManager lockNow().
     * Transparent, consent-based, official Android API.
     */
    fun lockDevice(): Boolean {
        return if (isAdminActive()) {
            dpm.lockNow()
            true
        } else {
            false
        }
    }
}
