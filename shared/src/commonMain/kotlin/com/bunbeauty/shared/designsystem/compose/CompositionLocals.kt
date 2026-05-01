package com.bunbeauty.shared.designsystem.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalBottomBarPadding = compositionLocalOf { 0.dp }

@Composable
fun Modifier.bottomBarPadding(bottom: Dp = 0.dp): Modifier =
    if (getIsImeVisible()) {
        this
    } else {
        this.padding(bottom = bottom + LocalBottomBarPadding.current)
    }

@Composable
fun getIsImeVisible() = WindowInsets.ime.getBottom(LocalDensity.current) > 0
