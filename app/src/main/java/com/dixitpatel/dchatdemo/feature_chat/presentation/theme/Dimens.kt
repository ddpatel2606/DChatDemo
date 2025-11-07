package com.dixitpatel.dchatdemo.feature_chat.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val s: Dp = 12.dp,
    val m: Dp = 16.dp,
    val ml: Dp = 18.dp,
    val l: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 40.dp,
    val xl3: Dp = 48.dp,
    val xl4: Dp = 56.dp,
    val xl5: Dp = 64.dp
)

val LocalDimens = compositionLocalOf { AppDimensions() }

val MaterialTheme.dimens: AppDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current
