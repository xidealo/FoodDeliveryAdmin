package com.bunbeauty.fooddeliveryadmin.compose.element.text_field

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminTextFieldDefaults {

    val textFieldColors: TextFieldColors
        @Composable get() = TextFieldDefaults.colors(
            disabledTextColor = AdminTheme.colors.main.onSurfaceVariant,
            focusedContainerColor = AdminTheme.colors.main.surface,
            unfocusedContainerColor = AdminTheme.colors.main.surface,
            cursorColor = AdminTheme.colors.main.primary,
            errorCursorColor = AdminTheme.colors.main.error,
            focusedIndicatorColor = AdminTheme.colors.main.primary,
            unfocusedIndicatorColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledIndicatorColor = AdminTheme.colors.main.onSurfaceVariant,
            errorIndicatorColor = AdminTheme.colors.main.error,
            focusedLeadingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledLeadingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            errorLeadingIconColor = AdminTheme.colors.main.error,
            focusedTrailingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledTrailingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            errorTrailingIconColor = AdminTheme.colors.main.error,
            focusedLabelColor = AdminTheme.colors.main.primary,
            unfocusedLabelColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledLabelColor = AdminTheme.colors.main.onSurfaceVariant,
            errorLabelColor = AdminTheme.colors.main.error,
            disabledPlaceholderColor = AdminTheme.colors.main.onSurfaceVariant
        )

    val textSelectionColors: TextSelectionColors
        @Composable get() = TextSelectionColors(
            handleColor = AdminTheme.colors.main.primary,
            backgroundColor = AdminTheme.colors.main.primary.copy(alpha = 0.4f)
        )
}
