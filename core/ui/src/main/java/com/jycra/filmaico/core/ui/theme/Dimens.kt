package com.jycra.filmaico.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimens(
    val logoSize: Dp,
    val horizontalBrowseCardWidth: Dp,
    val horizontalBrowseCardHeight: Dp,
    val verticalBrowseCardWidth: Dp,
    val verticalBrowseCardHeight: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val horizontalContentCardWidth: Dp,
    val horizontalContentCardHeight: Dp,
    val verticalContentCardWidth: Dp,
    val verticalContentCardHeight: Dp,
)

val MobileDimens = AppDimens(
    logoSize = 24.dp,
    horizontalBrowseCardWidth = 192.dp,
    horizontalBrowseCardHeight = 128.dp,
    verticalBrowseCardWidth = 128.dp,
    verticalBrowseCardHeight = 192.dp,
    coverWidth = 128.dp,
    coverHeight = 192.dp,
    horizontalContentCardWidth = 144.dp,
    horizontalContentCardHeight = 81.dp,
    verticalContentCardWidth = 192.dp,
    verticalContentCardHeight = 108.dp
)

val TvDimens = AppDimens(
    logoSize = 18.dp,
    horizontalBrowseCardWidth = 192.dp,
    horizontalBrowseCardHeight = 128.dp,
    verticalBrowseCardWidth = 128.dp,
    verticalBrowseCardHeight = 192.dp,
    coverWidth = 160.dp,
    coverHeight = 224.dp,
    horizontalContentCardWidth = 144.dp,
    horizontalContentCardHeight = 81.dp,
    verticalContentCardWidth = 192.dp,
    verticalContentCardHeight = 108.dp
)

val LocalAppDimens = staticCompositionLocalOf { MobileDimens }