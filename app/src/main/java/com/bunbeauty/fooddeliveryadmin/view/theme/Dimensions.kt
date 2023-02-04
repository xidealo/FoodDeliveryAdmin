package com.bunbeauty.fooddeliveryadmin.view.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }

data class AppDimensions(
    val verySmallSpace: Dp = 4.dp,
    val smallSpace: Dp = 8.dp,
    val mediumSpace: Dp = 16.dp,
    val elevation: Dp = 1.dp,
    val cardHeight: Dp = 40.dp,
    val buttonSize: Dp = 40.dp,
    val smallButtonSize: Dp = 32.dp,
    val addressEndSpace: Dp = 32.dp,
    val productImageSmallHeight: Dp = 72.dp,
    val productImageSmallWidth: Dp = 108.dp,
    val blurHeight: Dp = 16.dp,
    val smallProgressBarSize: Dp = 24.dp,
) {
    fun getItemSpaceByIndex(i: Int): Dp {
        return if (i == 0) {
            0.dp
        } else {
            smallSpace
        }
    }

    fun getEvaluation(hasShadow: Boolean): Dp {
        return if (hasShadow) {
            elevation
        } else {
            0.dp
        }
    }


}
