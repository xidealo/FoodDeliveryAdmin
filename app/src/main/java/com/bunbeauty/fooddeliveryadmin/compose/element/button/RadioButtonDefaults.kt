package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object RadioButtonDefaults {
    val radioButtonColors: RadioButtonColors
        @Composable get() =
            RadioButtonDefaults.colors(
                selectedColor = AdminTheme.colors.main.primary,
                unselectedColor = AdminTheme.colors.main.onSurfaceVariant,
            )
}
