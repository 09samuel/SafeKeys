package com.example.safekeys.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = LightGray,
    onPrimaryContainer = Black,
    secondary = LightGray,
    onSecondary = Black,
    secondaryContainer = LightGray,
    onSecondaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = Black,
    onError = White,
    outline = LightGray,
    inverseOnSurface = White,
    inverseSurface = Black,
    inversePrimary = White,
    surfaceTint = Black,
)

// Dark Theme Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = DarkerGray,
    onPrimaryContainer = OffWhite,
    secondary = DarkGray,
    onSecondary = White,
    secondaryContainer = DarkGray,
    onSecondaryContainer = White,
    background = Black,
    onBackground = OffWhite,
    surface = DarkGray,
    onSurface = OffWhite,
    surfaceVariant = DarkerGray,
    onSurfaceVariant = OffWhite,
    onError = Black,
    outline = DarkGray,
    inverseOnSurface = Black,
    inverseSurface = White,
    inversePrimary = Black,
    surfaceTint = White,
)

@Composable
fun SafeKeysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}