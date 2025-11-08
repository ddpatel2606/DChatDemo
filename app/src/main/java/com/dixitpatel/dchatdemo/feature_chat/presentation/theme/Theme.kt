package com.dixitpatel.dchatdemo.feature_chat.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * A Composable function that applies the application's theme to the UI content.
 *
 * This theme wrapper sets up the color scheme, typography, and custom dimensions
 * for the descendant Composable. It supports both light and dark themes, as well as
 * dynamic theming on Android 12 and above.
 *
 * @param darkTheme Whether the theme should be in dark mode. Defaults to the system's setting.
 * @param dynamicColor Whether to use a dynamic color scheme based on the user's wallpaper.
 *                     This is only available on Android 12 and newer. Defaults to `true`.
 * @param content The Composable content to which the theme will be applied.
 */
@Composable
fun DChatDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    CompositionLocalProvider(
        LocalDimens provides AppDimensions(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    tertiary = LightTertiary,
    inversePrimary = LightInversePrimary
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    tertiary = DarkTertiary,
    inversePrimary = DarkInversePrimary
)
