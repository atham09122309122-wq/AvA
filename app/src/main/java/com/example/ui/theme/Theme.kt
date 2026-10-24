package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AvaDarkColorScheme = darkColorScheme(
    primary = AvaPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = AvaPurpleContainer,
    onPrimaryContainer = AvaPurpleLight,
    secondary = AvaPurpleLight,
    onSecondary = Color.Black,
    secondaryContainer = AvaCardBorder,
    onSecondaryContainer = AvaTextPrimary,
    tertiary = AvaGold,
    onTertiary = Color.Black,
    background = AvaBlack,
    onBackground = AvaTextPrimary,
    surface = AvaSurface,
    onSurface = AvaTextPrimary,
    surfaceVariant = AvaCard,
    onSurfaceVariant = AvaTextSecondary,
    outline = AvaCardBorder,
    outlineVariant = AvaDivider,
    error = AvaError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // AVA Studio is a signature premium dark creative studio
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AvaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
