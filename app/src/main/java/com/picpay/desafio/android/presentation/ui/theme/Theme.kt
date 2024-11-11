package com.picpay.desafio.android.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PicPayPrimary,
    onPrimary = PicPayOnPrimaryLight,
    primaryContainer = PicPayPrimaryVariant,
    secondary = PicPaySecondary,
    onSecondary = PicPayOnSecondaryLight,
    background = PicPayBackgroundLight,
    onBackground = PicPayOnBackgroundLight,
    surface = PicPaySurfaceLight,
    onSurface = PicPayOnSurfaceLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PicPayPrimary,
    onPrimary = PicPayOnPrimaryDark,
    primaryContainer = PicPayPrimaryVariant,
    secondary = PicPaySecondary,
    onSecondary = PicPayOnSecondaryDark,
    background = PicPayBackgroundDark,
    onBackground = PicPayOnBackgroundDark,
    surface = PicPaySurfaceDark,
    onSurface = PicPayOnSurfaceDark,
)

private val HighContrastColorPalette = lightColorScheme(
    primary = PicPayPrimaryHighContrast,
    onPrimary = PicPayOnPrimaryHighContrast,
    primaryContainer = PicPayPrimaryVariantHighContrast,
    secondary = PicPaySecondaryHighContrast,
    onSecondary = PicPayOnSecondaryHighContrast,
    background = PicPayBackgroundHighContrast,
    onBackground = PicPayOnBackgroundHighContrast,
    surface = PicPaySurfaceHighContrast,
    onSurface = PicPayOnSurfaceHighContrast,
)

@Composable
fun DesafioAndroidPicPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isHighContrast -> HighContrastColorPalette
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
