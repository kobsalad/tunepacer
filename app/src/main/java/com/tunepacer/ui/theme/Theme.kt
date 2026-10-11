package com.tunepacer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE1289F),
    // secondary
    // tertiary

    background = Color.hsl(321f, 0.2f, 0.04f),
    surface = Color.hsl(321f, 0.2f, 0.04f),
    surfaceContainer = Color.hsl(321f, 0.2f, 0.08f),
    surfaceContainerHigh = Color.hsl(321f, 0.2f, 0.14f),

    onPrimary = Color.White,
    // onSecondary
    // onTertiary
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
