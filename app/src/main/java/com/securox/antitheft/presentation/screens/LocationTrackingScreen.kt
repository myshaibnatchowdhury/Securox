package com.securox.antitheft.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.MetricCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LocationTrackingScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val targetDevice by viewModel.selectedTargetDevice.collectAsState()
    val isTrackingRunning by viewModel.isLocationTrackingRunning.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Live Location Radar",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (targetDevice != null) {
            val dev = targetDevice!!
            val lat = dev.lastLatitude ?: 37.7749
            val lng = dev.lastLongitude ?: -122.4194
            val accuracy = dev.lastAccuracy ?: 4.5f
            val lastTime = dev.lastLocationTime ?: dev.lastSeen

            // Cyber Radar Map Visualizer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SecuroxCard)
                    .border(BorderStroke(1.dp, SecuroxNeonPrimary.copy(alpha = 0.6f)), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Radar Grid & Sweeper
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val maxRadius = size.height * 0.42f

                    // Concentric Range Rings
                    drawCircle(Color(0xFF1D3524), radius = maxRadius * 0.33f, center = center, style = Stroke(1f))
                    drawCircle(Color(0xFF1D3524), radius = maxRadius * 0.66f, center = center, style = Stroke(1f))
                    drawCircle(Color(0xFF2E573B), radius = maxRadius, center = center, style = Stroke(1.5f))

                    // Crosshairs
                    drawLine(Color(0xFF1D3524), Offset(center.x, center.y - maxRadius), Offset(center.x, center.y + maxRadius), strokeWidth = 1f)
                    drawLine(Color(0xFF1D3524), Offset(center.x - maxRadius, center.y), Offset(center.x + maxRadius, center.y), strokeWidth = 1f)

                    // Rotating Radar Scanner Beam
                    val rad = Math.toRadians(sweepAngle.toDouble())
                    val endX = center.x + (maxRadius * cos(rad)).toFloat()
                    val endY = center.y + (maxRadius * sin(rad)).toFloat()
                    drawLine(
                        color = Color(0xFF39FF14).copy(alpha = 0.7f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 2.5f
                    )

                    // Target Blip (Device Location)
                    val blipX = center.x + 35f
                    val blipY = center.y - 25f
                    drawCircle(Color(0x5539FF14), radius = 18f, center = Offset(blipX, blipY))
                    drawCircle(Color(0xFF39FF14), radius = 6f, center = Offset(blipX, blipY))
                }

                // Overlay Metadata Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecuroxSurface.copy(alpha = 0.85f))
                        .border(BorderStroke(1.dp, SecuroxCardBorder), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "GPS LOCK: ±${accuracy.toInt()}m ACCURACY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecuroxNeonPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SecuroxSurface.copy(alpha = 0.85f))
                        .border(BorderStroke(1.dp, SecuroxCardBorder), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FUSED LOCATION PROVIDER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecuroxMetallicSilver
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coordinates Metric Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    title = "Latitude",
                    value = String.format("%.5f°", lat),
                    icon = Icons.Default.Navigation,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Longitude",
                    value = String.format("%.5f°", lng),
                    icon = Icons.Default.Navigation,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Telemetry Card
            CyberCard {
                Column {
                    DetailItem("Target Device", dev.deviceName)
                    DetailItem("Provider", "Google Fused Location API")
                    DetailItem("Network State", if (isOnline) "Connected (Cellular/Wi-Fi)" else "Offline Cache")
                    DetailItem("Last Fix Time", SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault()).format(Date(lastTime)))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Locate Action Button
            NeonButton(
                text = "LOCATE MY PHONE NOW",
                icon = Icons.Default.MyLocation,
                onClick = { viewModel.triggerLocateDevice(dev) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Foreground Service Toggle (for local physical device)
            if (dev.isCurrentDevice) {
                Button(
                    onClick = {
                        viewModel.toggleForegroundLocationTracking(!isTrackingRunning)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTrackingRunning) Color(0xFF2A1515) else SecuroxCard,
                        contentColor = if (isTrackingRunning) SecuroxDanger else SecuroxNeonPrimary
                    ),
                    border = BorderStroke(1.dp, if (isTrackingRunning) SecuroxDanger else SecuroxCardBorder)
                ) {
                    Icon(
                        imageVector = if (isTrackingRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTrackingRunning) "STOP BACKGROUND TRACKING SERVICE" else "START PERSISTENT FOREGROUND TRACKING",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
