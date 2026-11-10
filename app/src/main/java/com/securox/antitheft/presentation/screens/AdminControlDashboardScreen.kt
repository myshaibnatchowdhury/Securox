package com.securox.antitheft.presentation.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SimCardAlert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.core.theme.SecuroxBg
import com.securox.antitheft.core.theme.SecuroxCard
import com.securox.antitheft.core.theme.SecuroxCardBorder
import com.securox.antitheft.core.theme.SecuroxCardBorderHighlight
import com.securox.antitheft.core.theme.SecuroxDanger
import com.securox.antitheft.core.theme.SecuroxMetallicSilver
import com.securox.antitheft.core.theme.SecuroxNeonPrimary
import com.securox.antitheft.core.theme.SecuroxNeonSecondary
import com.securox.antitheft.core.theme.SecuroxSurface
import com.securox.antitheft.core.theme.SecuroxTextMuted
import com.securox.antitheft.core.theme.SecuroxTextPrimary
import com.securox.antitheft.core.theme.SecuroxTextSecondary
import com.securox.antitheft.core.theme.SecuroxWarning
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.presentation.viewmodel.LockExecutionPhase
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminControlDashboardScreen(
    viewModel: SecuroxViewModel,
    onNavigateToEmergencyLock: () -> Unit,
    onNavigateToLocationTracking: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val devices by viewModel.devices.collectAsState()
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()
    val events by viewModel.events.collectAsState()
    val lockStatus by viewModel.lockStatus.collectAsState()

    var showWipeConfirmDialog by remember { mutableStateOf(false) }
    var actionFeedback by remember { mutableStateOf<String?>(null) }
    var isDispatchingAction by remember { mutableStateOf(false) }

    // Fallback device if devices list is loaded but targetDevice is null
    val effectiveDevice = targetDevice ?: devices.firstOrNull() ?: DeviceInfo(
        deviceId = "local_phone",
        deviceName = "My Android Phone",
        manufacturer = "Android",
        model = "Protected Device",
        androidVersion = "14",
        isOnline = true,
        batteryLevel = 100,
        isAdminActive = true,
        isDeviceOwner = false,
        protectionStatus = "PROTECTED"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP HEADER
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SecuroxNeonPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ADMIN CONTROL DASHBOARD",
                        color = SecuroxNeonPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Master Anti-Theft & Device Command Center",
                        color = SecuroxTextSecondary,
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { viewModel.refreshHardwareStatus() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = SecuroxNeonPrimary
                    )
                }
            }
        }

        // DEVICE SELECTOR TABS
        item {
            Column {
                Text(
                    text = "SELECT TARGET MANAGED DEVICE",
                    color = SecuroxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (devices.isEmpty()) {
                    Text(
                        text = "Using Local Phone Profile (Add more devices anytime).",
                        color = SecuroxTextMuted,
                        fontSize = 13.sp
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(devices) { dev ->
                            val isSelected = (dev.deviceId == effectiveDevice.deviceId)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) SecuroxNeonPrimary.copy(alpha = 0.15f) else SecuroxCard)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) SecuroxNeonPrimary else SecuroxCardBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.selectTargetDevice(dev.deviceId) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = dev.deviceName,
                                            color = if (isSelected) SecuroxNeonPrimary else SecuroxTextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = if (dev.isCurrentDevice) "This Device" else dev.model,
                                            color = SecuroxTextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TARGET DEVICE TELEMETRY CARD
        item {
            val dev = effectiveDevice
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SecuroxCard),
                border = BorderStroke(1.dp, SecuroxCardBorderHighlight),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = dev.deviceName,
                                color = SecuroxWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${dev.manufacturer} • ${dev.model} • Android ${dev.androidVersion}",
                                color = SecuroxTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (dev.isOnline) SecuroxNeonPrimary.copy(alpha = 0.15f) else SecuroxDanger.copy(alpha = 0.15f))
                            .border(1.dp, if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (dev.isOnline) "● ONLINE" else "○ OFFLINE",
                                color = if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // TELEMETRY GRID
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TelemetryPill(
                            icon = if (dev.batteryLevel > 20) Icons.Default.BatteryChargingFull else Icons.Default.BatteryAlert,
                            label = "Battery",
                            value = "${dev.batteryLevel}%",
                            highlight = dev.batteryLevel > 20
                        )
                        TelemetryPill(
                            icon = Icons.Default.AdminPanelSettings,
                            label = "Device Admin",
                            value = if (dev.isAdminActive) "ACTIVE" else "DISABLED",
                            highlight = dev.isAdminActive
                        )
                        TelemetryPill(
                            icon = Icons.Default.Security,
                            label = "Device Owner",
                            value = if (dev.isDeviceOwner) "ENROLLED" else "UNMANAGED",
                            highlight = dev.isDeviceOwner
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Protection Status: ${dev.protectionStatus}",
                            color = SecuroxNeonSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Last Seen: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(dev.lastSeen))}",
                            color = SecuroxTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // PRIMARY EMERGENCY COMMAND: EMERGENCY LOCK
        item {
            val dev = effectiveDevice
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SecuroxDanger.copy(alpha = 0.08f)),
                border = BorderStroke(1.5.dp, SecuroxDanger.copy(alpha = pulseGlow)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Emergency Lock",
                            tint = SecuroxDanger,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "REMOTE EMERGENCY LOCK",
                                color = SecuroxDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Instantly locks the target phone screen via Android DevicePolicyManager",
                                color = SecuroxTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (dev.isCurrentDevice) {
                                viewModel.startEmergencyLockSequence()
                                viewModel.confirmAndSendEmergencyLock(dev)
                            } else {
                                onNavigateToEmergencyLock()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SecuroxDanger),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.LockClock, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXECUTE EMERGENCY LOCK NOW",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (lockStatus.phase != LockExecutionPhase.IDLE) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Status: ${lockStatus.phase.name} — ${lockStatus.message}",
                            color = if (lockStatus.phase == LockExecutionPhase.EXECUTED) SecuroxNeonPrimary else SecuroxWarning,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // SPECIAL STOLEN PHONE CONTROL SUITE (ফোন চুরি হলেও নিয়ন্ত্রণ নিশ্চিতকরণ)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2417)),
                border = BorderStroke(1.5.dp, SecuroxNeonPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SecuroxNeonPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = SecuroxNeonPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "THEFT PERSISTENCE SHIELD",
                                color = SecuroxNeonPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "ফোন চুরি হলেও সর্বদা আপনার সম্পূর্ণ নিয়ন্ত্রণে রাখার ব্যবস্থা",
                                color = SecuroxNeonSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Feature 1: SIM Protection
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.SimCardAlert, contentDescription = null, tint = SecuroxWarning, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "সিম পরিবর্তন প্রতিরোধ: অপরিচিত সিম ঢোকালেই ফোন অটোমেটিক লক ও নতুন নম্বর সার্ভারে প্রেরণ।",
                            color = SecuroxTextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Feature 2: Force Siren
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.NotificationImportant, contentDescription = null, tint = SecuroxDanger, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "সাইলেন্ট মোড বাইপাস: সাইলেন্ট বা DND থাকলেও সর্বোচ্চ ভলিউমে সাইরেন বাজানো যাবে।",
                            color = SecuroxTextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Feature 3: Anti-Reset
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.PowerOff, contentDescription = null, tint = SecuroxNeonPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "হার্ডওয়্যার শাটডাউন ও ফ্যাক্টরি রিসেট ব্লক: সেটিংস থেকে রিসেট বা সেফ বুট বন্ধ রাখা।",
                            color = SecuroxTextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // ADMIN CONTROL COMMAND GRID
        item {
            Text(
                text = "REMOTE COMMAND CENTER",
                color = SecuroxTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        item {
            val dev = effectiveDevice
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1: Locate & Sound Alarm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.GpsFixed,
                        title = "Locate Device",
                        desc = "Request real-time GPS coordinate fix",
                        accentColor = SecuroxNeonPrimary,
                        onClick = {
                            viewModel.triggerLocateDevice(dev)
                            onNavigateToLocationTracking()
                        }
                    )

                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        title = "Sound Alarm",
                        desc = "Trigger loud anti-theft acoustic siren",
                        accentColor = SecuroxWarning,
                        onClick = {
                            isDispatchingAction = true
                            viewModel.sendAdminAction(dev, AppConstants.CMD_SOUND_ALARM) { _, msg ->
                                isDispatchingAction = false
                                actionFeedback = msg
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                // Row 2: Anti-Shutdown & Enforce FRP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.PowerOff,
                        title = "Anti-Shutdown",
                        desc = "Arm boot & power tripwire monitor",
                        accentColor = SecuroxNeonSecondary,
                        onClick = {
                            isDispatchingAction = true
                            viewModel.sendAdminAction(dev, AppConstants.CMD_ENTER_LOCKDOWN) { _, msg ->
                                isDispatchingAction = false
                                actionFeedback = msg
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Shield,
                        title = "Enforce FRP",
                        desc = "Block factory reset & safe boot",
                        accentColor = SecuroxNeonPrimary,
                        onClick = {
                            viewModel.toggleEnterpriseRestrictions(true)
                            viewModel.sendAdminAction(dev, AppConstants.CMD_ENABLE_RESTRICTIONS) { _, msg ->
                                actionFeedback = msg
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                // Row 3: Ping Device & Remote Wipe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Refresh,
                        title = "Ping Device",
                        desc = "Request battery & network telemetry",
                        accentColor = SecuroxMetallicSilver,
                        onClick = {
                            viewModel.sendAdminAction(dev, AppConstants.CMD_REFRESH_DEVICE_STATUS) { _, msg ->
                                actionFeedback = msg
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    AdminActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.DeleteForever,
                        title = "Remote Wipe",
                        desc = "Erase personal data (Enterprise)",
                        accentColor = SecuroxDanger,
                        onClick = {
                            showWipeConfirmDialog = true
                        }
                    )
                }
            }
        }

        // FEEDBACK BANNER
        actionFeedback?.let { fb ->
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecuroxSurface)
                        .border(1.dp, SecuroxNeonPrimary, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SecuroxNeonPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = fb, color = SecuroxTextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        // ANDROID ENTERPRISE ADB PROVISIONING GUIDE
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SecuroxSurface),
                border = BorderStroke(1.dp, SecuroxCardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SecuroxNeonPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ANDROID ENTERPRISE DEVICE OWNER SETUP",
                            color = SecuroxNeonPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "To grant SECUROX hardware-level anti-theft enforcement (disable factory reset, disallow safe boot), run this ADB command once:",
                        color = SecuroxTextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val adbCmd = "adb shell dpm set-device-owner com.securox.antitheft/.security.SecuroxDeviceAdminReceiver"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = adbCmd,
                                color = SecuroxNeonPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(adbCmd))
                                Toast.makeText(context, "ADB command copied!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = SecuroxTextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // WEB DASHBOARD INFORMATION
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SecuroxCard),
                border = BorderStroke(1.dp, SecuroxNeonSecondary.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🌐 WEB ADMIN CONTROL DASHBOARD AVAILABLE",
                        color = SecuroxNeonSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ফোন হারিয়ে গেলে আপনার অন্য কোনো ডিভাইস না থাকলেও যেকোনো কম্পিউটার, ল্যাপটপ বা যেকোনো ব্রাউজার থেকে SECUROX Web Control Dashboard (web/index.html) খুলে আপনার ইমেইল ও পাসওয়ার্ড দিয়ে দূর থেকেই স্ক্রিন লক, লাইভ জিপিএস লোকেশন ও সাইরেন অ্যালার্ম বাজাতে পারবেন।",
                        color = SecuroxTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // RECENT SECURITY AUDIT TRAIL
        item {
            Text(
                text = "REAL-TIME SECURITY AUDIT LOG",
                color = SecuroxTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        if (events.isEmpty()) {
            item {
                Text(
                    text = "No security events recorded yet.",
                    color = SecuroxTextMuted,
                    fontSize = 12.sp
                )
            }
        } else {
            items(events.take(6)) { ev ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecuroxSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                when (ev.severity) {
                                    "CRITICAL" -> SecuroxDanger
                                    "WARNING" -> SecuroxWarning
                                    else -> SecuroxNeonPrimary
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ev.eventType,
                            color = SecuroxTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = ev.description,
                            color = SecuroxTextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ev.timestamp)),
                        color = SecuroxTextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // REMOTE WIPE CONFIRMATION DIALOG
    if (showWipeConfirmDialog) {
        val dev = effectiveDevice
        AlertDialog(
            onDismissRequest = { showWipeConfirmDialog = false },
            title = {
                Text(
                    text = "Confirm Remote Wipe",
                    color = SecuroxDanger,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "WARNING: This command will factory reset and erase all user data on '${dev.deviceName}'. This action cannot be undone. Are you sure you wish to proceed?",
                    color = SecuroxTextPrimary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showWipeConfirmDialog = false
                        viewModel.sendAdminAction(dev, AppConstants.CMD_WIPE_DATA) { _, msg ->
                            actionFeedback = msg
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecuroxDanger)
                ) {
                    Text("YES, WIPE DEVICE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirmDialog = false }) {
                    Text("CANCEL", color = SecuroxTextSecondary)
                }
            },
            containerColor = SecuroxSurface,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun TelemetryPill(
    icon: ImageVector,
    label: String,
    value: String,
    highlight: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SecuroxSurface)
            .border(1.dp, if (highlight) SecuroxNeonPrimary.copy(alpha = 0.4f) else SecuroxCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (highlight) SecuroxNeonPrimary else SecuroxTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = value,
                    color = if (highlight) SecuroxNeonPrimary else SecuroxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = label,
                color = SecuroxTextMuted,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun AdminActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    desc: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SecuroxCard),
        border = BorderStroke(1.dp, SecuroxCardBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = title,
                color = SecuroxTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                color = SecuroxTextMuted,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
