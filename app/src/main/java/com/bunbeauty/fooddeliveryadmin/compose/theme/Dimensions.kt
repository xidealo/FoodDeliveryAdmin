package com.bunbeauty.fooddeliveryadmin.compose.theme

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalAdminDimensions = staticCompositionLocalOf { AdminDimensions() }

data class AdminDimensions(
    // old
    val verySmallSpace: Dp = 4.dp,
    val smallSpace: Dp = 8.dp,
    val mediumSpace: Dp = 16.dp,
    val elevation: Dp = 1.dp,
    val smallButtonSize: Dp = 32.dp,
    val addressEndSpace: Dp = 32.dp,
    val productImageSmallHeight: Dp = 72.dp,
    val productImageSmallWidth: Dp = 108.dp,
    val blurHeight: Dp = 16.dp,
    val smallProgressBarSize: Dp = 24.dp,
    // new
    val cardElevation: Dp = 2.dp,
    val surfaceElevation: Dp = 4.dp,
    val buttonRadius: Dp = 20.dp,
    val cardRadius: Dp = 8.dp,
    val bottomSheetRadius: Dp = 16.dp,
    val screenContentSpace: Dp = 16.dp,
    val codeWidth: Dp = 56.dp,
    val scrollScreenBottomSpace: Dp = ButtonDefaults.MinHeight + 32.dp,
    val snackBarPadding: Dp = ButtonDefaults.MinHeight + 12.dp,
)
