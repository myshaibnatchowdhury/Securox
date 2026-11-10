package com.securox.antitheft.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.R
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
import com.securox.antitheft.data.model.DeviceInfo
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.CyberTopBar
import com.securox.antitheft.presentation.components.MetricCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.components.StatusBadge
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: SecuroxViewModel,
    onNavigateToEmergencyLock: () -> Unit,
    onNavigateToLocation: () -> Unit,
    onNavigateToSecurityCenter: () -> Unit,
    onNavigateToDevices: () -> Unit,
    onNavigateToAntiShutdown: () -> Unit,
    onNavigateToDeviceOwner: () -> Unit,
    onNavigateToSimSecurity: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDeviceDetails: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit = {}
) {
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val userFeedback by viewModel.userFeedback.collectAsState()
    val isCloudMode by viewModel.isCloudMode.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userFeedback) {
        userFeedback?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedback()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
    ) {
        CyberTopBar(
            title = "SECUROX",
            subtitle = if (isCloudMode) "Cloud Mode: Firebase Online" else "Local Shield Test Mode",
            onNotificationClick = onNavigateToAlerts,
            onProfileClick = onNavigateToSettings
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Brand Banner Card
            item {
                CyberCard(glow = true, borderColor = SecuroxNeonPrimary) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isOnline) SecuroxNeonPrimary else SecuroxDanger)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isOnline) "SYSTEM ONLINE" else "OFFLINE SHIELD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOnline) SecuroxNeonPrimary else SecuroxDanger
                                )
                            }
                            Text(
                                text = "MILITARY SPEC 256-BIT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SecuroxMetallicSilver
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Your Phone. Our Protection.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp
                            ),
                            color = SecuroxWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "SECUROX keeps your Android device safe, secure and under your control — even if it's lost or stolen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecuroxTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Target Protected Device Card
            item {
                if (targetDevice != null) {
                    val dev = targetDevice!!
                    CyberCard(
                        onClick = onNavigateToDeviceDetails,
                        borderColor = if (dev.isOnline) SecuroxCardBorderHighlight else SecuroxDanger.copy(alpha = 0.5f)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Smartphone,
                                        contentDescription = null,
                                        tint = SecuroxNeonPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = dev.deviceName,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = SecuroxWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${dev.manufacturer} • ${dev.model}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                            color = SecuroxTextSecondary
                                        )
                                    }
                                }
                                StatusBadge(status = dev.protectionStatus)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Device quick stats row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricCard(
                                    title = "Battery",
                                    value = "${dev.batteryLevel}%",
                                    icon = if (dev.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryStd,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Status",
                                    value = if (dev.isOnline) "ONLINE" else "OFFLINE",
                                    icon = if (dev.isOnline) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    accentColor = if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Admin",
                                    value = if (dev.isAdminActive) "ACTIVE" else "OFF",
                                    icon = Icons.Default.Shield,
                                    accentColor = if (dev.isAdminActive) SecuroxNeonPrimary else SecuroxWarning,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                } else {
                    CyberCard {
                        Text(
                            text = "No device enrolled yet. Tap below to register a phone.",
                            color = SecuroxTextSecondary
                        )
                    }
                }
            }

            // VIP Admin Control Dashboard Highlight
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigateToAdminDashboard() },
                    colors = CardDefaults.cardColors(containerColor = SecuroxSurface),
                    border = BorderStroke(1.5.dp, SecuroxNeonPrimary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(SecuroxNeonPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = SecuroxNeonPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ADMIN CONTROL DASHBOARD",
                                    color = SecuroxNeonPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Master remote command center & sirens",
                                    color = SecuroxTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = SecuroxNeonPrimary
                        )
                    }
                }
            }

            // Primary Emergency Action Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Massive Red Emergency Lock Button
                    Button(
                        onClick = onNavigateToEmergencyLock,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecuroxDanger,
                            contentColor = SecuroxWhite
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFFF6B6B)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SecuroxWhite,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "EMERGENCY LOCK",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ),
                                color = SecuroxWhite
                            )
                        }
                    }

                    // Secondary Quick Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateToLocation,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SecuroxCard,
                                contentColor = SecuroxNeonPrimary
                            ),
                            border = BorderStroke(1.dp, SecuroxNeonPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LOCATE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onNavigateToSecurityCenter,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SecuroxCard,
                                contentColor = SecuroxWhite
                            ),
                            border = BorderStroke(1.dp, SecuroxCardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SecuroxMetallicSilver,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SECURITY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Feature Navigation Grid
            item {
                Text(
                    text = "SECURITY MODULES",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = SecuroxWhite,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FeatureTile(
                            title = "Remote Lock",
                            desc = "Instant lockdown",
                            icon = Icons.Default.Lock,
                            onClick = onNavigateToEmergencyLock,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureTile(
                            title = "Live Location",
                            desc = "Real-time GPS",
                            icon = Icons.Default.GpsFixed,
                            onClick = onNavigateToLocation,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FeatureTile(
                            title = "Anti-Power-Off",
                            desc = "Disconnection alert",
                            icon = Icons.Default.PowerOff,
                            onClick = onNavigateToAntiShutdown,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureTile(
                            title = "Device Owner",
                            desc = "Enterprise policies",
                            icon = Icons.Default.Shield,
                            onClick = onNavigateToDeviceOwner,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FeatureTile(
                            title = "SIM Security",
                            desc = "Swap & removal guard",
                            icon = Icons.Default.SimCard,
                            onClick = onNavigateToSimSecurity,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureTile(
                            title = "Security Alerts",
                            desc = "Audit event log",
                            icon = Icons.Default.Notifications,
                            onClick = onNavigateToAlerts,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FeatureTile(
    title: String,
    desc: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SecuroxCard),
        border = BorderStroke(1.dp, SecuroxCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x2239FF14)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SecuroxNeonPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = SecuroxWhite
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = SecuroxTextSecondary
                )
            }
        }
    }
}

@Composable
fun MyDevicesScreen(
    viewModel: SecuroxViewModel,
    onNavigateToRegisterDevice: () -> Unit,
    onNavigateToDetails: (DeviceInfo) -> Unit,
    onBackClick: () -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val selectedId by viewModel.selectedDeviceId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "My Registered Devices",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }
            IconButton(onClick = onNavigateToRegisterDevice) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = SecuroxNeonPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select any registered phone or tablet to control it remotely.",
            style = MaterialTheme.typography.bodyMedium,
            color = SecuroxTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(devices) { device ->
                val isSelected = (selectedId == device.deviceId)
                CyberCard(
                    glow = isSelected,
                    borderColor = if (isSelected) SecuroxNeonPrimary else SecuroxCardBorder,
                    onClick = {
                        viewModel.selectTargetDevice(device.deviceId)
                        onNavigateToDetails(device)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0x3339FF14) else SecuroxSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = if (isSelected) SecuroxNeonPrimary else SecuroxMetallicSilver
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = device.deviceName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = SecuroxWhite
                                    )
                                    if (device.isCurrentDevice) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(THIS PHONE)",
                                            color = SecuroxNeonSecondary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "${device.manufacturer} ${device.model} • ${if (device.isOnline) "Online" else "Offline"}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = if (device.isOnline) SecuroxNeonSecondary else SecuroxDanger
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            StatusBadge(status = device.protectionStatus)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Battery ${device.batteryLevel}%",
                                fontSize = 11.sp,
                                color = SecuroxTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterDeviceScreen(
    viewModel: SecuroxViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enroll New Device",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Register another Android phone or tablet under your SECUROX master account. You can subsequently sign in on that phone or send security commands to it from here.",
                style = MaterialTheme.typography.bodyMedium,
                color = SecuroxTextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Device Nickname (e.g. Work Phone)", color = SecuroxTextSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecuroxNeonPrimary,
                    unfocusedBorderColor = SecuroxCardBorder,
                    focusedTextColor = SecuroxWhite,
                    unfocusedTextColor = SecuroxWhite
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Manufacturer & Model (e.g. Pixel 8)", color = SecuroxTextSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecuroxNeonPrimary,
                    unfocusedBorderColor = SecuroxCardBorder,
                    focusedTextColor = SecuroxWhite,
                    unfocusedTextColor = SecuroxWhite
                )
            )
        }

        NeonButton(
            text = "COMPLETE ENROLLMENT",
            enabled = name.isNotBlank(),
            onClick = {
                viewModel.registerNewDevice(name, model) {
                    onSuccess()
                }
            }
        )
    }
}

@Composable
fun DeviceDetailsScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit,
    onNavigateToLock: () -> Unit,
    onNavigateToLocation: () -> Unit
) {
    val device by viewModel.selectedTargetDevice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Device Specifications",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }
            if (device != null && !device!!.isCurrentDevice) {
                IconButton(onClick = { viewModel.removeDevice(device!!.deviceId); onBackClick() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = SecuroxDanger)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (device != null) {
            val dev = device!!
            CyberCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = dev.deviceName,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = SecuroxWhite
                            )
                            Text(
                                text = "Device ID: ${dev.deviceId}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                color = SecuroxTextSecondary
                            )
                        }
                        StatusBadge(status = dev.protectionStatus)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailItem("Manufacturer", dev.manufacturer)
                    DetailItem("Model", dev.model)
                    DetailItem("Android Version", dev.androidVersion)
                    DetailItem("App Version", dev.appVersion)
                    DetailItem("Online Status", if (dev.isOnline) "CONNECTED" else "DISCONNECTED")
                    DetailItem("Battery Level", "${dev.batteryLevel}% ${if (dev.isCharging) "(Charging)" else ""}")
                    DetailItem("Device Administrator", if (dev.isAdminActive) "ACTIVE" else "INACTIVE")
                    DetailItem("Enterprise Device Owner", if (dev.isDeviceOwner) "PROVISIONED" else "NOT PROVISIONED")
                    DetailItem("SIM State", dev.simState)
                    DetailItem("Mobile Carrier", dev.carrierName)
                    DetailItem("Last Seen", SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(Date(dev.lastSeen)))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            NeonButton(
                text = "EMERGENCY LOCK THIS DEVICE",
                isDanger = true,
                icon = Icons.Default.Lock,
                onClick = onNavigateToLock
            )

            Spacer(modifier = Modifier.height(10.dp))

            NeonButton(
                text = "TRACK LIVE GPS LOCATION",
                icon = Icons.Default.GpsFixed,
                onClick = onNavigateToLocation
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = SecuroxTextSecondary, fontSize = 13.sp)
        Text(text = value, color = SecuroxWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
