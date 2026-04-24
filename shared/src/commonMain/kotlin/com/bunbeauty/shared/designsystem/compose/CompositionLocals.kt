package com.bunbeauty.shared.designsystem.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalBottomBarPadding = compositionLocalOf { 0.dp }

@Composable
fun Modifier.bottomBarPadding(bottom: Dp = 0.dp): Modifier = this.padding(bottom = bottom + LocalBottomBarPadding.current)
