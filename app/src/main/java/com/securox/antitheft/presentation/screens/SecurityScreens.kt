package com.securox.antitheft.presentation.screens

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.core.theme.SecuroxBg
import com.securox.antitheft.core.theme.SecuroxCard
import com.securox.antitheft.core.theme.SecuroxCardBorder
import com.securox.antitheft.core.theme.SecuroxCardBorderHighlight
import com.securox.antitheft.core.theme.SecuroxDanger
import com.securox.antitheft.core.theme.SecuroxMetallicSilver
import com.securox.antitheft.core.theme.SecuroxNeonPrimary
import com.securox.antitheft.core.theme.SecuroxNeonSecondary
import com.securox.antitheft.core.theme.SecuroxSurface
import com.securox.antitheft.core.theme.SecuroxTextPrimary
import com.securox.antitheft.core.theme.SecuroxTextSecondary
import com.securox.antitheft.core.theme.SecuroxWarning
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel

@Composable
fun SecurityCenterScreen(
    viewModel: SecuroxViewModel,
    onNavigateToDeviceAdmin: () -> Unit,
    onNavigateToDeviceOwner: () -> Unit,
    onNavigateToAntiShutdown: () -> Unit,
    onNavigateToFactoryReset: () -> Unit,
    onNavigateToSimSecurity: () -> Unit,
    onBackClick: () -> Unit
) {
    val isAdminActive by viewModel.isAdminActive.collectAsState()
    val isDeviceOwner by viewModel.isDeviceOwner.collectAsState()

    val securityScore = when {
        isAdminActive && isDeviceOwner -> 100
        isAdminActive || isDeviceOwner -> 80
        else -> 45
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Security Command Center",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Posture Score Card
        CyberCard(glow = true, borderColor = if (securityScore >= 80) SecuroxNeonPrimary else SecuroxWarning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "SECURITY POSTURE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecuroxNeonPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (securityScore == 100) "Full Enterprise Shield" else "Standard Protection",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SecuroxWhite
                    )
                    Text(
                        text = if (securityScore >= 80) "Hardware lock & telemetry active" else "Administrator mode recommended",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = SecuroxTextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(SecuroxSurface)
                        .border(BorderStroke(3.dp, if (securityScore >= 80) SecuroxNeonPrimary else SecuroxWarning), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$securityScore%",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = if (securityScore >= 80) SecuroxNeonPrimary else SecuroxWarning
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HARDWARE & POLICY CONTROLS",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = SecuroxWhite
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Defense modules list
        SecurityControlItem(
            title = "Device Administrator",
            subtitle = "Immediate screen locking via lockNow()",
            isActive = isAdminActive,
            icon = Icons.Default.Lock,
            onClick = onNavigateToDeviceAdmin
        )

        Spacer(modifier = Modifier.height(10.dp))

        SecurityControlItem(
            title = "Android Enterprise Device Owner",
            subtitle = "Restricts factory reset and safe boot bypasses",
            isActive = isDeviceOwner,
            icon = Icons.Default.Shield,
            onClick = onNavigateToDeviceOwner
        )

        Spacer(modifier = Modifier.height(10.dp))

        SecurityControlItem(
            title = "Anti-Power-Off Protection",
            subtitle = "Power unplug & battery drain monitoring",
            isActive = true,
            icon = Icons.Default.PowerOff,
            onClick = onNavigateToAntiShutdown
        )

        Spacer(modifier = Modifier.height(10.dp))

        SecurityControlItem(
            title = "Factory Reset Protection",
            subtitle = "Enterprise DPM reset blocker & recovery limits",
            isActive = isDeviceOwner,
            icon = Icons.Default.Warning,
            onClick = onNavigateToFactoryReset
        )

        Spacer(modifier = Modifier.height(10.dp))

        SecurityControlItem(
            title = "SIM Card Security",
            subtitle = "Detects SIM removal and hardware carrier changes",
            isActive = true,
            icon = Icons.Default.SimCard,
            onClick = onNavigateToSimSecurity
        )

        Spacer(modifier = Modifier.height(10.dp))

        SecurityControlItem(
            title = "Hardware Keystore Encryption",
            subtitle = "AES-GCM-256 authenticated encryption keys",
            isActive = true,
            icon = Icons.Default.Key,
            onClick = {}
        )
    }
}

@Composable
fun SecurityControlItem(
    title: String,
    subtitle: String,
    isActive: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    CyberCard(
        onClick = onClick,
        borderColor = if (isActive) SecuroxCardBorderHighlight else SecuroxCardBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isActive) Color(0x2239FF14) else Color(0x22C8D1D9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isActive) SecuroxNeonPrimary else SecuroxMetallicSilver,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                        color = SecuroxWhite
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        color = SecuroxTextSecondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isActive) Color(0x2239FF14) else Color(0x22FF3434))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "SETUP",
                    color = if (isActive) SecuroxNeonPrimary else SecuroxDanger,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun DeviceAdminSetupScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val isAdminActive by viewModel.isAdminActive.collectAsState()
    val context = LocalContext.current

    val adminLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.refreshHardwareStatus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Device Administrator Mode",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CyberCard(
                borderColor = if (isAdminActive) SecuroxNeonPrimary else SecuroxWarning
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "ADMINISTRATOR PRIVILEGES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAdminActive) SecuroxNeonPrimary else SecuroxWarning
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAdminActive) "Activated & Operational" else "Activation Required",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SecuroxWhite
                        )
                    }
                    Icon(
                        imageVector = if (isAdminActive) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isAdminActive) SecuroxNeonPrimary else SecuroxWarning,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "WHAT DEVICE ADMINISTRATOR ENABLES:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                color = SecuroxNeonSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            CyberCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FeatureCheckItem("Immediate Screen Locking", "Enables SECUROX to lock your stolen phone instantly via Android DevicePolicyManager.lockNow() upon receiving your remote command.")
                    FeatureCheckItem("Tamper Detection", "Notifies the owner immediately if an attacker attempts to deactivate protection in Android Settings.")
                    FeatureCheckItem("Failed Passcode Monitoring", "Detects consecutive failed passcode attempts and triggers security alert logs.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TRANSPARENCY & PRIVACY NOTICE:\nSECUROX Device Administrator never accesses personal files, photos, or passwords. You can deactivate it at any time in Android Settings > Security.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = SecuroxTextSecondary,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        NeonButton(
            text = if (isAdminActive) "TEST LOCAL LOCK NOW" else "ACTIVATE DEVICE ADMINISTRATOR",
            icon = Icons.Default.Lock,
            onClick = {
                if (isAdminActive) {
                    viewModel.securityManager.lockDevice()
                } else {
                    val intent = viewModel.securityManager.createAdminActivationIntent()
                    adminLauncher.launch(intent)
                }
            }
        )
    }
}

@Composable
fun DeviceOwnerSetupScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val isDeviceOwner by viewModel.isDeviceOwner.collectAsState()
    val context = LocalContext.current
    val adbCommand = viewModel.ownerManager.getAdbProvisioningCommand()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Enterprise Device Owner Mode",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(borderColor = if (isDeviceOwner) SecuroxNeonPrimary else SecuroxCardBorder) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ANDROID ENTERPRISE STATUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDeviceOwner) SecuroxNeonPrimary else SecuroxMetallicSilver
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isDeviceOwner) "Device Owner Active" else "Not Provisioned (Standard Mode)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SecuroxWhite
                        )
                    }
                    Icon(
                        imageVector = if (isDeviceOwner) Icons.Default.CheckCircle else Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (isDeviceOwner) SecuroxNeonPrimary else SecuroxMetallicSilver,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Android Enterprise Device Owner gives SECUROX official system policy control to prevent factory resets and safe-boot tampering.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = SecuroxTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "DEVELOPMENT & TEST PROVISIONING GUIDE",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
            color = SecuroxNeonSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        CyberCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Official ADB Setup Command (No Root Required):",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                    color = SecuroxWhite
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecuroxSurface)
                        .border(BorderStroke(1.dp, SecuroxCardBorder), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = adbCommand,
                            color = SecuroxNeonPrimary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("SECUROX ADB Command", adbCommand))
                                Toast.makeText(context, "Command copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = SecuroxNeonPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Prerequisites: Connect phone via USB with USB Debugging enabled. Ensure no accounts exist on the device before executing the command (Android Enterprise security requirement).",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = SecuroxTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isDeviceOwner) {
            NeonButton(
                text = "TOGGLE ENTERPRISE RESTRICTIONS",
                onClick = { viewModel.toggleEnterpriseRestrictions(true) }
            )
        }
    }
}

@Composable
fun AntiShutdownScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Anti-Shutdown & Disconnect Guard",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(borderColor = SecuroxNeonPrimary) {
            Column {
                Text(
                    text = "ANTI-POWER-OFF PROTECTION STATUS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecuroxNeonPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Active Hardware Monitoring",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SecuroxWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Monitors charger disconnection, low battery threshold, and logs telemetry prior to potential offline state.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = SecuroxTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureCheckItem("Power Cable Disconnect Detection", "Logs an instant security event if a thief unplugs the phone from charging.")
                FeatureCheckItem("Critical Low Battery Snapshot", "Captures last known GPS coordinates and battery telemetry before phone turns off.")
                FeatureCheckItem("Boot & Restart Detection", "Automatically sends a reconnection alert to the owner as soon as the device powers on.")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Honest hardware limitation disclaimer
        CyberCard(borderColor = SecuroxWarning) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = SecuroxWarning, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Hardware Button Transparency Note",
                        fontWeight = FontWeight.Bold,
                        color = SecuroxWarning,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No non-system application can physically disable the physical power button or prevent forced hard reboot (10-second hold) on ordinary consumer Android hardware. SECUROX ensures the device logs its last location, alerts the owner upon disconnection, and immediately re-engages protection upon boot.",
                        color = SecuroxTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FactoryResetProtectionScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val isDeviceOwner by viewModel.isDeviceOwner.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Factory Reset Protection",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(borderColor = if (isDeviceOwner) SecuroxNeonPrimary else SecuroxCardBorder) {
            Column {
                Text(
                    text = "ENTERPRISE POLICY STATUS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDeviceOwner) SecuroxNeonPrimary else SecuroxMetallicSilver
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isDeviceOwner) "DISALLOW_FACTORY_RESET: ENFORCED" else "Requires Android Enterprise Provisioning",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SecuroxWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Where Device Owner mode is provisioned, SECUROX blocks factory reset in Settings and prevents safe-boot mode tampering.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = SecuroxTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(borderColor = SecuroxCardBorder) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "System Limitations & Bootloader Transparency:",
                    fontWeight = FontWeight.Bold,
                    color = SecuroxWhite,
                    fontSize = 13.sp
                )
                Text(
                    text = "A standard Android application cannot guarantee prevention of hardware-level Recovery Mode wipe or bootloader unlocking without OEM manufacturer hardware locks. SECUROX implements 100% of the officially supported Android enterprise policies.",
                    color = SecuroxTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun SimSecurityScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SIM Card Security",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (targetDevice != null) {
            val dev = targetDevice!!
            CyberCard(borderColor = if (dev.simState == "READY") SecuroxNeonPrimary else SecuroxDanger) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MONITORED SIM CARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecuroxNeonPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Carrier: ${dev.carrierName}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SecuroxWhite
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (dev.simState == "READY") Color(0x2239FF14) else Color(0x22FF3434))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = dev.simState,
                                color = if (dev.simState == "READY") SecuroxNeonPrimary else SecuroxDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    DetailItem("SIM Serial (Last 4)", dev.simSerialLast4)
                    DetailItem("Hardware State", if (dev.simState == "READY") "Operational / Active" else "SIM Ejected / Missing")
                    DetailItem("Swap Alarm Status", "ARMED (Instant Push Notification)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CyberCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Theft Detection Protocol:",
                        fontWeight = FontWeight.Bold,
                        color = SecuroxWhite,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "If a thief physically removes or swaps the SIM card, SECUROX logs an immediate CRITICAL security event and alerts all your other linked devices through the cloud backend.",
                        color = SecuroxTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureCheckItem(title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = SecuroxNeonPrimary,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = SecuroxWhite, fontSize = 13.sp)
            Text(text = desc, color = SecuroxTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
