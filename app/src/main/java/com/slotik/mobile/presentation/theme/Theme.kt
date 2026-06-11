package com.slotik.mobile.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors: ColorScheme = lightColorScheme(
    primary = SlotikPrimary,
    onPrimary = SlotikSurface,
    background = SlotikBackground,
    onBackground = SlotikTextPrimary,
    surface = SlotikSurface,
    onSurface = SlotikTextPrimary,
    surfaceVariant = SlotikSurfaceAlt,
    onSurfaceVariant = SlotikTextSecondary,
    outline = SlotikDivider,
    error = SlotikDanger,
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = SlotikPrimaryLight,
    onPrimary = SlotikTextPrimary,
    background = SlotikTextPrimary,
    onBackground = SlotikSurface,
    surface = SlotikTextSecondary,
    onSurface = SlotikSurface,
    surfaceVariant = SlotikMuted,
    onSurfaceVariant = SlotikSurface,
    outline = SlotikDivider,
    error = SlotikDanger,
)

@Composable
fun SlotikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = SlotikTypography,
        content = content,
    )
}
