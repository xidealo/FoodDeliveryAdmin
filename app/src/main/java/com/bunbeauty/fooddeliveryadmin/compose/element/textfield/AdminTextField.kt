package com.bunbeauty.fooddeliveryadmin.compose.element.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.applyIfNotNull
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardActions
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun AdminTextField(
    value: String,
    labelText: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = keyboardOptions(),
    keyboardActions: KeyboardActions = keyboardActions(),
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        AdminBaseTextField(
            modifier = Modifier
                .fillMaxWidth()
                .applyIfNotNull(focusRequester) {
                    focusRequester(it)
                },
            value = value,
            labelText = labelText,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxSymbols = maxSymbols,
            maxLines = maxLines,
            isError = isError,
            enabled = enabled,
            trailingIcon = trailingIcon
        )
        ErrorText(
            isError = isError,
            errorText = errorText
        )
    }
}

@Composable
fun AdminTextField(
    value: TextFieldValue,
    labelText: String,
    onValueChange: (value: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    keyboardOptions: KeyboardOptions = keyboardOptions(),
    keyboardActions: KeyboardActions = keyboardActions(),
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        AdminBaseTextField(
            modifier = Modifier
                .fillMaxWidth()
                .applyIfNotNull(focusRequester) {
                    focusRequester(it)
                },
            value = value,
            labelText = labelText,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxSymbols = maxSymbols,
            maxLines = maxLines,
            isError = isError,
            enabled = enabled,
            trailingIcon = trailingIcon
        )
        ErrorText(
            isError = isError,
            errorText = errorText
        )
    }
}

@Composable
private fun ErrorText(
    isError: Boolean,
    errorText: String?
) {
    if (isError && errorText != null) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp),
            text = errorText,
            style = AdminTheme.typography.bodySmall,
            color = AdminTheme.colors.main.error
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun FoodDeliveryTextFieldPreview() {
    AdminTheme {
        AdminTextField(
            labelText = "Комментарий",
            value = "Нужно больше еды \n ...",
            onValueChange = {}
        )
    }
}
