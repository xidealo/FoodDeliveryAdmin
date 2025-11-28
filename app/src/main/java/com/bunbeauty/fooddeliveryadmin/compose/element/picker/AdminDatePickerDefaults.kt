package com.bunbeauty.fooddeliveryadmin.compose.element.picker

import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults

object AdminDatePickerDefaults {
    val datePickerColors: DatePickerColors
        @Composable get() =
            DatePickerDefaults.colors(
                headerBackgroundColor = AdminTheme.colors.main.surface,
                headerTextColor = AdminTheme.colors.main.onSurface,
                calendarHeaderTextColor = AdminTheme.colors.main.onSurface,
                dateActiveBackgroundColor =
                    AdminTheme.colors.main.primary
                        .copy(0.2f),
                dateInactiveBackgroundColor = AdminTheme.colors.main.surface,
                dateActiveTextColor = AdminTheme.colors.main.primary,
                dateInactiveTextColor = AdminTheme.colors.main.onSurface,
            )
}
