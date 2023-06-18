package com.bunbeauty.fooddeliveryadmin.compose.element.text_field

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminTextFieldDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    val textFieldColors: TextFieldColors
        @Composable get() = TextFieldDefaults.textFieldColors(
            //  textColor = AdminTheme.colors.mainColors.onSurface,
            disabledTextColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            containerColor = AdminTheme.colors.mainColors.surface,
            cursorColor = AdminTheme.colors.mainColors.primary,
            errorCursorColor = AdminTheme.colors.mainColors.error,
            focusedIndicatorColor = AdminTheme.colors.mainColors.primary,
            unfocusedIndicatorColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledIndicatorColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorIndicatorColor = AdminTheme.colors.mainColors.error,
            focusedLeadingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledLeadingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorLeadingIconColor = AdminTheme.colors.mainColors.error,
            focusedTrailingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledTrailingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorTrailingIconColor = AdminTheme.colors.mainColors.error,
            focusedLabelColor = AdminTheme.colors.mainColors.primary,
            unfocusedLabelColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledLabelColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorLabelColor = AdminTheme.colors.mainColors.error,
            // placeholderColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledPlaceholderColor = AdminTheme.colors.mainColors.onSurfaceVariant
        )

    @OptIn(ExperimentalMaterial3Api::class)
    val smsCodeTextFieldColors: TextFieldColors
        @Composable get() = TextFieldDefaults.textFieldColors(
            // textColor = AdminTheme.colors.mainColors.onSurface,
            disabledTextColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            containerColor = AdminTheme.colors.mainColors.surface,
            cursorColor = Color.Transparent,
            errorCursorColor = AdminTheme.colors.mainColors.error,
            focusedIndicatorColor = AdminTheme.colors.mainColors.primary,
            unfocusedIndicatorColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledIndicatorColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorIndicatorColor = AdminTheme.colors.mainColors.error,
            focusedLeadingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledLeadingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorLeadingIconColor = AdminTheme.colors.mainColors.error,
            focusedTrailingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledTrailingIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorTrailingIconColor = AdminTheme.colors.mainColors.error,
            focusedLabelColor = AdminTheme.colors.mainColors.primary,
            unfocusedLabelColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledLabelColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            errorLabelColor = AdminTheme.colors.mainColors.error,
            // placeholderColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledPlaceholderColor = AdminTheme.colors.mainColors.onSurfaceVariant
        )

    val textSelectionColors: TextSelectionColors
        @Composable get() = TextSelectionColors(
            handleColor = AdminTheme.colors.mainColors.primary,
            backgroundColor = AdminTheme.colors.mainColors.primary.copy(alpha = 0.4f)
        )
}
