@file:Suppress("DEPRECATION")

package com.jycra.filmaico.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.jycra.filmaico.core.ui.theme.color.backgroundDark
import com.jycra.filmaico.core.ui.theme.color.backgroundDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.backgroundDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.backgroundLight
import com.jycra.filmaico.core.ui.theme.color.backgroundLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.backgroundLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.errorContainerDark
import com.jycra.filmaico.core.ui.theme.color.errorContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.errorContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.errorContainerLight
import com.jycra.filmaico.core.ui.theme.color.errorContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.errorContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.errorDark
import com.jycra.filmaico.core.ui.theme.color.errorDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.errorDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.errorLight
import com.jycra.filmaico.core.ui.theme.color.errorLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.errorLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceDark
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceLight
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.inverseOnSurfaceLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryDark
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryLight
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.inversePrimaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceDark
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceLight
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.inverseSurfaceLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onBackgroundDark
import com.jycra.filmaico.core.ui.theme.color.onBackgroundDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onBackgroundDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onBackgroundLight
import com.jycra.filmaico.core.ui.theme.color.onBackgroundLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onBackgroundLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerDark
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerLight
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorDark
import com.jycra.filmaico.core.ui.theme.color.onErrorDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorLight
import com.jycra.filmaico.core.ui.theme.color.onErrorLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onErrorLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryDark
import com.jycra.filmaico.core.ui.theme.color.onPrimaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryLight
import com.jycra.filmaico.core.ui.theme.color.onPrimaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onPrimaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryDark
import com.jycra.filmaico.core.ui.theme.color.onSecondaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryLight
import com.jycra.filmaico.core.ui.theme.color.onSecondaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSecondaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceDark
import com.jycra.filmaico.core.ui.theme.color.onSurfaceDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceLight
import com.jycra.filmaico.core.ui.theme.color.onSurfaceLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantDark
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantLight
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onSurfaceVariantLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryDark
import com.jycra.filmaico.core.ui.theme.color.onTertiaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryLight
import com.jycra.filmaico.core.ui.theme.color.onTertiaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.onTertiaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.outlineDark
import com.jycra.filmaico.core.ui.theme.color.outlineDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.outlineDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.outlineLight
import com.jycra.filmaico.core.ui.theme.color.outlineLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.outlineLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.outlineVariantDark
import com.jycra.filmaico.core.ui.theme.color.outlineVariantDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.outlineVariantDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.outlineVariantLight
import com.jycra.filmaico.core.ui.theme.color.outlineVariantLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.outlineVariantLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.primaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.primaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.primaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.primaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.primaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.primaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.primaryDark
import com.jycra.filmaico.core.ui.theme.color.primaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.primaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.primaryLight
import com.jycra.filmaico.core.ui.theme.color.primaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.primaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.scrimDark
import com.jycra.filmaico.core.ui.theme.color.scrimDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.scrimDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.scrimLight
import com.jycra.filmaico.core.ui.theme.color.scrimLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.scrimLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryDark
import com.jycra.filmaico.core.ui.theme.color.secondaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryLight
import com.jycra.filmaico.core.ui.theme.color.secondaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.secondaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightDark
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightLight
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceBrightLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerDark
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighDark
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighLight
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestDark
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestLight
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerHighestLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLight
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowDark
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowLight
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestDark
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestLight
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceContainerLowestLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDark
import com.jycra.filmaico.core.ui.theme.color.surfaceDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDimDark
import com.jycra.filmaico.core.ui.theme.color.surfaceDimDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDimDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDimLight
import com.jycra.filmaico.core.ui.theme.color.surfaceDimLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceDimLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceLight
import com.jycra.filmaico.core.ui.theme.color.surfaceLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantDark
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantLight
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.surfaceVariantLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerDark
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerLight
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryContainerLightMediumContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryDark
import com.jycra.filmaico.core.ui.theme.color.tertiaryDarkHighContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryDarkMediumContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryLight
import com.jycra.filmaico.core.ui.theme.color.tertiaryLightHighContrast
import com.jycra.filmaico.core.ui.theme.color.tertiaryLightMediumContrast
import com.jycra.filmaico.core.ui.theme.font.TypographyMobile
import com.jycra.filmaico.core.ui.theme.font.TypographyTv

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Composable
fun FilmaicoTheme(
    platform: String = "mobile",
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    val typography = when (platform) {
        "mobile" -> TypographyMobile
        "tv" -> TypographyTv
        else -> TypographyMobile
    }

    val dimens = when (platform) {
        "mobile" -> MobileDimens
        "tv" -> TvDimens
        else -> MobileDimens
    }

    CompositionLocalProvider(LocalAppDimens provides dimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }

}