package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AccentTheme {
    GOLD, GREEN, BLUE, RED
}

fun getAccentColor(accentTheme: AccentTheme): Color {
    return when (accentTheme) {
        AccentTheme.GOLD -> GoldPrimary
        AccentTheme.GREEN -> AccentGreen
        AccentTheme.BLUE -> AccentBlue
        AccentTheme.RED -> AccentRed
    }
}

private val BaseDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = PureBlack,
    secondary = GoldSecondary,
    onSecondary = PureBlack,
    tertiary = AccentGreen,
    onTertiary = PureBlack,
    background = ObsidianBlack,
    onBackground = PureWhite,
    surface = CharcoalSurface,
    onSurface = PureWhite,
    surfaceVariant = Color(0xFF1E212D),
    onSurfaceVariant = TextMuted,
    outline = GlassBorderGold
)

private val BaseLightColorScheme = lightColorScheme(
    primary = Color(0xFFB8860B),
    onPrimary = PureWhite,
    secondary = Color(0xFF8B6508),
    onSecondary = PureWhite,
    tertiary = Color(0xFF2E7D32),
    background = Color(0xFFF7F7FA),
    onBackground = Color(0xFF101014),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101014),
    surfaceVariant = Color(0xFFF0F0F5),
    onSurfaceVariant = Color(0xFF505060),
    outline = Color(0xFFD0D0DD)
)

@Composable
fun Mission10LTheme(
    darkTheme: Boolean = true, // Dark mode by default
    accentTheme: AccentTheme = AccentTheme.GOLD,
    content: @Composable () -> Unit
) {
    val accent = getAccentColor(accentTheme)
    val colorScheme = if (darkTheme) {
        BaseDarkColorScheme.copy(
            primary = accent,
            secondary = accent.copy(alpha = 0.8f),
            outline = accent.copy(alpha = 0.35f)
        )
    } else {
        BaseLightColorScheme.copy(
            primary = accent,
            secondary = accent.copy(alpha = 0.8f)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

