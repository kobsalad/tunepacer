package com.tunepacer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFCC0080),
    secondary = Color(0xFF550000),
    tertiary = Color(0xFF005500),

    background = Color.hsl(321f, 0.2f, 0.04f),
    surface = Color.hsl(321f, 0.2f, 0.04f),
    surfaceContainer = Color.hsl(321f, 0.2f, 0.08f),
    surfaceContainerHigh = Color.hsl(321f, 0.2f, 0.14f),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun TunePacerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
