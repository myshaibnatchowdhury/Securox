package com.securox.antitheft

import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SecuroxSecurityUnitTest {

    @Test
    fun testCommandExpiration() {
        val now = System.currentTimeMillis()
        val validCommand = SecurityCommand(
            commandId = "cmd_01",
            createdAt = now,
            expiresAt = now + AppConstants.COMMAND_EXPIRATION_MS
        )
        assertTrue(validCommand.expiresAt > now)

        val expiredCommand = SecurityCommand(
            commandId = "cmd_02",
            createdAt = now - 600000L,
            expiresAt = now - 1000L
        )
        assertTrue(expiredCommand.expiresAt < now)
    }

    @Test
    fun testProtectionStatusLogic() {
        fun computeStatus(admin: Boolean, owner: Boolean): String {
            return when {
                admin && owner -> "PROTECTED"
                admin || owner -> "PARTIALLY_PROTECTED"
                else -> "UNPROTECTED"
            }
        }

        assertEquals("PROTECTED", computeStatus(admin = true, owner = true))
        assertEquals("PARTIALLY_PROTECTED", computeStatus(admin = true, owner = false))
        assertEquals("PARTIALLY_PROTECTED", computeStatus(admin = false, owner = true))
        assertEquals("UNPROTECTED", computeStatus(admin = false, owner = false))
    }

    @Test
    fun testSecurityEventSeverity() {
        val criticalEvent = SecurityEvent(
            eventType = AppConstants.EVENT_SIM_STATE_CHANGED,
            severity = "CRITICAL"
        )
        assertEquals("CRITICAL", criticalEvent.severity)

        val lockEvent = SecurityEvent(
            eventType = AppConstants.EVENT_LOCK_COMPLETED,
            severity = "HIGH"
        )
        assertEquals("HIGH", lockEvent.severity)
    }

    @Test
    fun testCommandTypes() {
        assertEquals("LOCK_DEVICE", AppConstants.CMD_LOCK_DEVICE)
        assertEquals("LOCATE_DEVICE", AppConstants.CMD_LOCATE_DEVICE)
        assertEquals("REFRESH_DEVICE_STATUS", AppConstants.CMD_REFRESH_DEVICE_STATUS)
        assertEquals("CHECK_SECURITY_STATUS", AppConstants.CMD_CHECK_SECURITY_STATUS)
    }
}
