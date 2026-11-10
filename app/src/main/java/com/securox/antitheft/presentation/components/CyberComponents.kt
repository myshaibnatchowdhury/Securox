package com.securox.antitheft.presentation.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.securox.antitheft.core.theme.SecuroxWarning
import com.securox.antitheft.core.theme.SecuroxWhite

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = SecuroxCardBorder,
    glow: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_glow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val strokeColor = if (glow) borderColor.copy(alpha = alpha) else borderColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SecuroxCard),
        border = BorderStroke(1.dp, strokeColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF14241A),
                            SecuroxCard
                        )
                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun NeonButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isDanger: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val bgColors = if (isDanger) {
        listOf(Color(0xFFFF3434), Color(0xFFC71515))
    } else {
        listOf(SecuroxNeonPrimary, SecuroxNeonSecondary)
    }

    val contentColor = if (isDanger) SecuroxWhite else SecuroxBg

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDanger) SecuroxDanger else SecuroxNeonPrimary,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFF1E2D22),
            disabledContentColor = Color(0xFF5A6E60)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        "PROTECTED" -> Triple(Color(0x2639FF14), SecuroxNeonPrimary, "PROTECTED")
        "PARTIALLY_PROTECTED" -> Triple(Color(0x26FFB020), SecuroxWarning, "PARTIALLY PROTECTED")
        else -> Triple(Color(0x26FF3434), SecuroxDanger, "NOT PROTECTED")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(BorderStroke(1.dp, textColor.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accentColor: Color = SecuroxNeonPrimary
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SecuroxSurface),
        border = BorderStroke(1.dp, SecuroxCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = SecuroxTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = SecuroxWhite
            )
        }
    }
}

@Composable
fun CyberTopBar(
    title: String = "SECUROX",
    subtitle: String? = "Your Phone. Our Protection.",
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecuroxBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SecuroxCard)
                    .border(BorderStroke(1.dp, SecuroxNeonPrimary), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Logo",
                    tint = SecuroxNeonPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SECURO",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = SecuroxWhite
                    )
                    Text(
                        text = "X",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = SecuroxNeonPrimary
                    )
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = SecuroxTextSecondary
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNotificationClick) {
                Box {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alerts",
                        tint = SecuroxTextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SecuroxNeonPrimary)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SecuroxCardBorderHighlight)
                    .border(BorderStroke(1.dp, SecuroxNeonPrimary), CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Profile",
                    tint = SecuroxNeonPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
