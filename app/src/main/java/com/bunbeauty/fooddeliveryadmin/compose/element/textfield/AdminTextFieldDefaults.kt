package com.bunbeauty.fooddeliveryadmin.compose.element.textfield

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminTextFieldDefaults {

    val textFieldColors: TextFieldColors
        @Composable get() = TextFieldDefaults.colors(
            focusedTextColor = AdminTheme.colors.main.onSurface,
            unfocusedTextColor = AdminTheme.colors.main.onSurface,
            disabledTextColor = AdminTheme.colors.main.onSurface,
            errorTextColor = AdminTheme.colors.main.error,
            focusedContainerColor = AdminTheme.colors.main.surface,
            unfocusedContainerColor = AdminTheme.colors.main.surface,
            disabledContainerColor = AdminTheme.colors.main.surface,
            errorContainerColor = AdminTheme.colors.main.surface,
            cursorColor = AdminTheme.colors.main.primary,
            errorCursorColor = AdminTheme.colors.main.error,
            selectionColors = TextSelectionColors(
                handleColor = AdminTheme.colors.main.primary.copy(0.2f),
                backgroundColor = AdminTheme.colors.main.primary.copy(0.2f)
            ),
            focusedIndicatorColor = AdminTheme.colors.main.primary,
            unfocusedIndicatorColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledIndicatorColor = AdminTheme.colors.main.onSurfaceVariant,
            errorIndicatorColor = AdminTheme.colors.main.error,
            focusedLeadingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            unfocusedLeadingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledLeadingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            errorLeadingIconColor = AdminTheme.colors.main.error,
            focusedTrailingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            unfocusedTrailingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledTrailingIconColor = AdminTheme.colors.main.onSurfaceVariant,
            errorTrailingIconColor = AdminTheme.colors.main.error,
            focusedLabelColor = AdminTheme.colors.main.primary,
            unfocusedLabelColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledLabelColor = AdminTheme.colors.main.onSurfaceVariant,
            errorLabelColor = AdminTheme.colors.main.error,
            focusedPlaceholderColor = AdminTheme.colors.main.primary,
            unfocusedPlaceholderColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledPlaceholderColor = AdminTheme.colors.main.onSurfaceVariant,
            errorPlaceholderColor = AdminTheme.colors.main.error,
            focusedSupportingTextColor = AdminTheme.colors.main.primary,
            unfocusedSupportingTextColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledSupportingTextColor = AdminTheme.colors.main.onSurfaceVariant,
            errorSupportingTextColor = AdminTheme.colors.main.error,
            focusedPrefixColor = AdminTheme.colors.main.primary,
            unfocusedPrefixColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledPrefixColor = AdminTheme.colors.main.onSurfaceVariant,
            errorPrefixColor = AdminTheme.colors.main.error,
            focusedSuffixColor = AdminTheme.colors.main.primary,
            unfocusedSuffixColor = AdminTheme.colors.main.onSurfaceVariant,
            disabledSuffixColor = AdminTheme.colors.main.onSurfaceVariant,
            errorSuffixColor = AdminTheme.colors.main.error
        )

    val textSelectionColors: TextSelectionColors
        @Composable get() = TextSelectionColors(
            handleColor = AdminTheme.colors.main.primary,
            backgroundColor = AdminTheme.colors.main.primary.copy(alpha = 0.4f)
        )

    fun keyboardOptions(
        autoCorrect: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Next
    ): KeyboardOptions {
        return KeyboardOptions(
            autoCorrect = autoCorrect,
            keyboardType = keyboardType,
            imeAction = imeAction
        )
    }

    fun keyboardActions(
        onDone: (KeyboardActionScope.() -> Unit)? = null
    ): KeyboardActions {
        return KeyboardActions(
            onDone = onDone
        )
    }

    @Composable
    fun RubleSymbol(modifier: Modifier = Modifier) {
        Text(
            modifier = modifier,
            text = RUBLE_CURRENCY,
            style = AdminTheme.typography.bodyLarge,
            color = AdminTheme.colors.main.onSurface
        )
    }

}
