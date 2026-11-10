package com.securox.antitheft.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// SECUROX Master Cyber Palette
val SecuroxBg = Color(0xFF030806)
val SecuroxSurface = Color(0xFF0A110D)
val SecuroxCard = Color(0xFF101A13)
val SecuroxCardBorder = Color(0xFF1D3524)
val SecuroxCardBorderHighlight = Color(0xFF2E573B)

val SecuroxNeonPrimary = Color(0xFF39FF14)      // High-voltage Neon Green
val SecuroxNeonSecondary = Color(0xFF8DFF62)    // Bright Accent Green
val SecuroxNeonMuted = Color(0xFF22990C)        // Deep Glow Green
val SecuroxMetallicSilver = Color(0xFFC8D1D9)   // Metallic Silver
val SecuroxMetallicBright = Color(0xFFE6EDF3)   // Titanium Highlight

val SecuroxWhite = Color(0xFFFFFFFF)
val SecuroxTextPrimary = Color(0xFFF0F6FC)
val SecuroxTextSecondary = Color(0xFF8B949E)
val SecuroxTextMuted = Color(0xFF57606A)

val SecuroxWarning = Color(0xFFFFB020)
val SecuroxDanger = Color(0xFFFF3434)
val SecuroxDangerGlow = Color(0xFF8A1414)
val SecuroxSuccess = Color(0xFF39FF14)
val SecuroxInfo = Color(0xFF58A6FF)

// Cyber Gradients
val SecuroxNeonGradient = Brush.horizontalGradient(
    colors = listOf(SecuroxNeonPrimary, SecuroxNeonSecondary)
)

val SecuroxDangerGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF3434), Color(0xFFFF6B6B))
)

val SecuroxCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF132218), Color(0xFF0D1710))
)

val SecuroxMetallicGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFE6EDF3), Color(0xFF8B949E), Color(0xFFC8D1D9))
)

val SecuroxHeroGlow = Brush.radialGradient(
    colors = listOf(Color(0x3339FF14), Color(0x00030806))
)
