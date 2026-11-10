package com.securox.antitheft.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.core.theme.SecuroxBg
import com.securox.antitheft.core.theme.SecuroxCard
import com.securox.antitheft.core.theme.SecuroxCardBorder
import com.securox.antitheft.core.theme.SecuroxCardBorderHighlight
import com.securox.antitheft.core.theme.SecuroxDanger
import com.securox.antitheft.core.theme.SecuroxNeonPrimary
import com.securox.antitheft.core.theme.SecuroxNeonSecondary
import com.securox.antitheft.core.theme.SecuroxSurface
import com.securox.antitheft.core.theme.SecuroxTextPrimary
import com.securox.antitheft.core.theme.SecuroxTextSecondary
import com.securox.antitheft.core.theme.SecuroxWarning
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.viewmodel.LockExecutionPhase
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel

@Composable
fun EmergencyLockScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()
    val lockState by viewModel.lockStatus.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "emergency_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Confirmation Modal
    if (lockState.phase == LockExecutionPhase.CONFIRMING && targetDevice != null) {
        val dev = targetDevice!!
        AlertDialog(
            onDismissRequest = { viewModel.cancelEmergencyLock() },
            containerColor = SecuroxCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = SecuroxDanger,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONFIRM EMERGENCY LOCK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = SecuroxWhite
                        )
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to remotely lock:",
                        color = SecuroxTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${dev.deviceName} (${dev.manufacturer} ${dev.model})",
                        color = SecuroxWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "This will immediately invoke DevicePolicyManager lockNow() on the device. An authorized passcode or biometric scan will be required to unlock it.",
                        color = SecuroxTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmAndSendEmergencyLock(dev) },
                    colors = ButtonDefaults.buttonColors(containerColor = SecuroxDanger)
                ) {
                    Text("YES, LOCK DEVICE NOW", fontWeight = FontWeight.Bold, color = SecuroxWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelEmergencyLock() }) {
                    Text("CANCEL", color = SecuroxTextSecondary)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Emergency Device Lock",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target Device Information Banner
            if (targetDevice != null) {
                val dev = targetDevice!!
                CyberCard(borderColor = if (dev.isOnline) SecuroxCardBorderHighlight else SecuroxDanger) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TARGET DEVICE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecuroxNeonPrimary,
                                letterSpacing = 1.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (dev.isOnline) "ONLINE" else "OFFLINE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (dev.isOnline) SecuroxNeonPrimary else SecuroxDanger
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dev.deviceName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SecuroxWhite
                        )
                        Text(
                            text = "${dev.manufacturer} ${dev.model} • Battery: ${dev.batteryLevel}%",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = SecuroxTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Interactive Massive Red Emergency Lock Button with Outer Radiant Glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                // Outer Pulse Ring
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .scale(if (lockState.phase == LockExecutionPhase.IDLE) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxNeonPrimary.copy(alpha = 0.25f)
                                    else SecuroxDanger.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Secondary Border Ring
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E0808))
                        .border(
                            BorderStroke(
                                3.dp,
                                if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxNeonPrimary
                                else SecuroxDanger
                            ),
                            CircleShape
                        )
                )

                // Actual Action Button
                Button(
                    onClick = {
                        if (targetDevice != null) {
                            viewModel.startEmergencyLockSequence()
                        }
                    },
                    enabled = lockState.phase != LockExecutionPhase.SENDING,
                    modifier = Modifier.size(160.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxNeonPrimary else SecuroxDanger,
                        contentColor = if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxBg else SecuroxWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = when (lockState.phase) {
                                LockExecutionPhase.EXECUTED -> Icons.Default.CheckCircle
                                LockExecutionPhase.SENDING -> Icons.Default.HourglassTop
                                else -> Icons.Default.Lock
                            },
                            contentDescription = null,
                            modifier = Modifier.size(46.dp),
                            tint = if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxBg else SecuroxWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (lockState.phase) {
                                LockExecutionPhase.EXECUTED -> "LOCKED"
                                LockExecutionPhase.SENDING -> "SENDING"
                                else -> "EMERGENCY\nLOCK"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            ),
                            textAlign = TextAlign.Center,
                            color = if (lockState.phase == LockExecutionPhase.EXECUTED) SecuroxBg else SecuroxWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Real-Time Command Pipeline Status Card
            AnimatedVisibility(
                visible = lockState.phase != LockExecutionPhase.IDLE,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CyberCard(
                    borderColor = when (lockState.phase) {
                        LockExecutionPhase.EXECUTED -> SecuroxNeonPrimary
                        LockExecutionPhase.SENDING -> SecuroxWarning
                        LockExecutionPhase.DELIVERED -> SecuroxNeonSecondary
                        LockExecutionPhase.OFFLINE -> SecuroxWarning
                        LockExecutionPhase.FAILED -> SecuroxDanger
                        else -> SecuroxCardBorder
                    }
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            when (lockState.phase) {
                                LockExecutionPhase.SENDING -> {
                                    CircularProgressIndicator(
                                        color = SecuroxWarning,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "STATUS: TRANSMITTING TO CLOUD BACKEND",
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxWarning,
                                        fontSize = 12.sp
                                    )
                                }
                                LockExecutionPhase.DELIVERED -> {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = null,
                                        tint = SecuroxNeonSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "STATUS: DELIVERED TO TARGET HARDWARE",
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxNeonSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                LockExecutionPhase.EXECUTED -> {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SecuroxNeonPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "STATUS: CONFIRMED EXECUTED",
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxNeonPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                                LockExecutionPhase.OFFLINE -> {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = SecuroxWarning,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "STATUS: TARGET OFFLINE (COMMAND QUEUED)",
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxWarning,
                                        fontSize = 12.sp
                                    )
                                }
                                LockExecutionPhase.FAILED -> {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = SecuroxDanger,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "STATUS: COMMAND EXECUTION FAILED",
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxDanger,
                                        fontSize = 12.sp
                                    )
                                }
                                else -> {}
                            }
                        }

                        if (lockState.message.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = lockState.message,
                                color = SecuroxTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }

                        if (lockState.command != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Command ID: ${lockState.command!!.commandId} • Type: ${lockState.command!!.commandType}",
                                color = SecuroxTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Bottom Security Disclaimer
        Text(
            text = "Official Device Administrator API lockNow(). Never performs unauthorized wipe or collects sensitive user files.",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = SecuroxTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
