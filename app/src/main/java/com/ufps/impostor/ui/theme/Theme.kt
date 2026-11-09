package com.ufps.impostor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// La web solo tiene modo claro, por eso no hay esquema oscuro ni color dinámico
private val ImpostorColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = Color.White,
    secondaryContainer = BgAccentLight,
    onSecondaryContainer = PrimaryRed,
    background = BgMain,
    onBackground = TextMain,
    surface = BgCard,
    onSurface = TextMain,
    surfaceVariant = BgCardSubtle,
    onSurfaceVariant = TextMuted,
    outline = BorderDark,
    outlineVariant = BorderLight
)

@Composable
fun ImpostorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ImpostorColorScheme,
        typography = Typography,
        content = content
    )
}
