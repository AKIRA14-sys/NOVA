package com.nova.browser.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CyberpunkDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = ElectricPurple,
    tertiary = NeonPink,
    background = DeepPurpleDark,
    surface = DeepPurpleCard,
    onPrimary = DeepPurpleDark,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceContainer = DeepPurpleBar
)

@Composable
fun NOVABrowserTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CyberpunkDarkColorScheme,
        typography = Typography,
        content = content
    )
}
