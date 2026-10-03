package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaffronLight,
    onPrimary = Color.Black,
    primaryContainer = SaffronDark,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF60A5FA),
    onSecondary = Color.Black,
    secondaryContainer = NavyLight,
    onSecondaryContainer = Color.White,
    tertiary = IndianGreenLight,
    onTertiary = Color.Black,
    tertiaryContainer = IndianGreen,
    onTertiaryContainer = Color.White,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    error = EmergencyRed,
    errorContainer = Color(0xFF7F1D1D)
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEDD5),
    onPrimaryContainer = Color(0xFF7C2D12),
    secondary = NavySecondary,
    onSecondary = Color.White,
    secondaryContainer = NavyContainer,
    onSecondaryContainer = NavySecondary,
    tertiary = IndianGreen,
    onTertiary = Color.White,
    tertiaryContainer = IndianGreenContainer,
    onTertiaryContainer = Color(0xFF052E16),
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    error = EmergencyRed,
    errorContainer = EmergencyRedContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep Bharat Mitra branding consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
