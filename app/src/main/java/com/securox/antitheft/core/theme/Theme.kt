package com.securox.antitheft.core.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SecuroxDarkColorScheme = darkColorScheme(
    primary = SecuroxNeonPrimary,
    onPrimary = SecuroxBg,
    primaryContainer = SecuroxCard,
    onPrimaryContainer = SecuroxNeonSecondary,
    secondary = SecuroxNeonSecondary,
    onSecondary = SecuroxBg,
    tertiary = SecuroxMetallicSilver,
    onTertiary = SecuroxBg,
    background = SecuroxBg,
    onBackground = SecuroxWhite,
    surface = SecuroxSurface,
    onSurface = SecuroxTextPrimary,
    surfaceVariant = SecuroxCard,
    onSurfaceVariant = SecuroxTextSecondary,
    error = SecuroxDanger,
    onError = SecuroxWhite,
    outline = SecuroxCardBorder,
    outlineVariant = SecuroxCardBorderHighlight
)

@Composable
fun SecuroxTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = SecuroxDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = SecuroxBg.toArgb()
            window.navigationBarColor = SecuroxBg.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SecuroxTypography,
        content = content
    )
}
