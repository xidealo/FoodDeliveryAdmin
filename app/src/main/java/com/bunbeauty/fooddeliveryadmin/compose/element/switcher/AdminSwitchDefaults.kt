package com.bunbeauty.fooddeliveryadmin.compose.element.switcher

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminSwitchDefaults {

    val switchColors: SwitchColors
        @Composable get() = SwitchDefaults.colors(
            checkedThumbColor = AdminTheme.colors.main.onPrimary,
            checkedTrackColor = AdminTheme.colors.main.primary,
            uncheckedThumbColor = AdminTheme.colors.main.onDisabled,
            uncheckedTrackColor = AdminTheme.colors.main.disabled,
            uncheckedBorderColor = AdminTheme.colors.main.onDisabled
        )
}
