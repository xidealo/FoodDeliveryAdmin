package com.bunbeauty.fooddeliveryadmin.compose.element.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun AdminBaseTextField(
    labelText: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides AdminTextFieldDefaults.textSelectionColors
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = value,
            onValueChange = { changedValue ->
                onValueChange(
                    changedValue.take(maxSymbols)
                )
            },
            textStyle = AdminTheme.typography.bodyLarge,
            label = {
                Text(
                    text = labelText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingIcon = {
                if (trailingIcon == null) {
                    if (value.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .clickable(
                                    onClick = {
                                        onValueChange("")
                                    }
                                ),
                            painter = painterResource(R.drawable.ic_clear),
                            tint = AdminTheme.colors.main.onSurfaceVariant,
                            contentDescription = null
                        )
                    }
                } else {
                    trailingIcon()
                }
            },
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            colors = AdminTextFieldDefaults.textFieldColors
        )
    }
}

@Composable
fun AdminBaseTextField(
    labelText: String,
    value: TextFieldValue,
    onValueChange: (value: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    isError: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides AdminTextFieldDefaults.textSelectionColors
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = value,
            onValueChange = { changedValue ->
                onValueChange(
                    changedValue.copy(
                        text = changedValue.text.take(maxSymbols)
                    )
                )
            },
            textStyle = AdminTheme.typography.bodyLarge,
            label = {
                Text(
                    text = labelText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingIcon = {
                if (trailingIcon == null) {
                    if (value.text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .clickable(
                                    onClick = {
                                        onValueChange(TextFieldValue(""))
                                    }
                                ),
                            painter = painterResource(R.drawable.ic_clear),
                            tint = AdminTheme.colors.main.onSurfaceVariant,
                            contentDescription = null
                        )
                    }
                } else {
                    trailingIcon()
                }
            },
            isError = isError,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            colors = AdminTextFieldDefaults.textFieldColors
        )
    }
}

@Preview
@Composable
private fun AdminBaseTextFieldPreview() {
    AdminTheme {
        AdminBaseTextField(
            labelText ="Комментарий",
            value = "Нужно больше еды \n ...",
            onValueChange = {}
        )
    }
}

@Preview
@Composable
private fun AdminBaseTextFieldWithErrorPreview() {
    AdminTheme {
        AdminBaseTextField(
            labelText ="Комментарий",
            value = "Нужно больше еды \n ...",
            onValueChange = {},
            isError = true
        )
    }
}

@Preview
@Composable
private fun AdminBaseTextFieldWithTrailingIconPreview() {
    AdminTheme {
        AdminBaseTextField(
            labelText ="Комментарий",
            value = "Нужно больше еды \n ...",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = null
                )
            }
        )
    }
}
