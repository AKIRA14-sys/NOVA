package com.nova.browser.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NeonCyan = Color(0xFF00F0FF)
val NeonPurple = Color(0xFF9D00FF)
val CyberDarkBackground = Color(0xFF0B0C10)
val CyberSurfaceDark = Color(0xFF1F2833)
val CyberCardDark = Color(0xFF12141D)
val NeonPink = Color(0xFFFF007F)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFC5C6C7)

private val NovaDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = CyberDarkBackground,
    surface = CyberSurfaceDark,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun NovaBrowserTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NovaDarkColorScheme,
        content = content
    )
}
