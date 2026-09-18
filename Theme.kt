package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NightmareDarkColorScheme = darkColorScheme(
    primary = NeonWhite,
    onPrimary = DarkBg,
    primaryContainer = DarkCardElevated,
    onPrimaryContainer = NeonWhite,
    secondary = NeonCyan,
    onSecondary = DarkBg,
    secondaryContainer = DarkCard,
    onSecondaryContainer = NeonCyan,
    tertiary = NeonPurple,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark neon style requested by user
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = NightmareDarkColorScheme,
        typography = Typography,
        content = content
    )
}

