package com.bunbeauty.fooddeliveryadmin.compose.element.switcher

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminSwitchDefaults {

    val switchColors: SwitchColors
        @Composable get() = SwitchDefaults.colors(
            checkedThumbColor = AdminTheme.colors.mainColors.onPrimary,
            checkedTrackColor = AdminTheme.colors.mainColors.primary,
            uncheckedThumbColor = AdminTheme.colors.mainColors.onDisabled,
            uncheckedTrackColor = AdminTheme.colors.mainColors.disabled,
            uncheckedBorderColor = AdminTheme.colors.mainColors.onDisabled,
        )
}
