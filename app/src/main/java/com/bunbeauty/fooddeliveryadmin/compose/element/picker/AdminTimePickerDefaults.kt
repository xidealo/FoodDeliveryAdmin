package com.bunbeauty.fooddeliveryadmin.compose.element.picker

import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.vanpra.composematerialdialogs.datetime.time.TimePickerColors
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults

object AdminTimePickerDefaults {

    val timePickerColors: TimePickerColors
        @Composable get() = TimePickerDefaults.colors(
            activeBackgroundColor = AdminTheme.colors.main.primary.copy(0.2f),
            inactiveBackgroundColor = AdminTheme.colors.main.disabled,
            activeTextColor = AdminTheme.colors.main.primary,
            inactiveTextColor = AdminTheme.colors.main.onSurface,
            selectorColor = AdminTheme.colors.main.primary,
            selectorTextColor = AdminTheme.colors.main.onPrimary,
            headerTextColor = AdminTheme.colors.main.onSurface,
            borderColor = AdminTheme.colors.main.onSurface
        )
}
