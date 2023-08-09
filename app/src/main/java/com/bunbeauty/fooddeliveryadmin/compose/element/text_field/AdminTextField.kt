package com.bunbeauty.fooddeliveryadmin.compose.element.text_field

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.applyIfNotNull
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun AdminTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    @StringRes labelStringId: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (value: String) -> Unit,
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    @StringRes errorMessageId: Int? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        FoodDeliveryBaseTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            labelStringId = labelStringId,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onValueChange = onValueChange,
            maxSymbols = maxSymbols,
            maxLines = maxLines,
            isError = errorMessageId != null,
            readOnly = readOnly,
            enabled = enabled
        )
        errorMessageId?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp),
                text = stringResource(errorMessageId),
                style = AdminTheme.typography.bodySmall,
                color = AdminTheme.colors.main.error
            )
        }
    }
}

@Composable
fun AdminTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    value: TextFieldValue = TextFieldValue(""),
    @StringRes labelStringId: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (value: TextFieldValue) -> Unit,
    maxSymbols: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    @StringRes errorMessageId: Int? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        FoodDeliveryBaseTextField(
            modifier = Modifier
                .fillMaxWidth()
                .applyIfNotNull(focusRequester) {
                    focusRequester(it)
                },
            value = value,
            labelStringId = labelStringId,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onValueChange = onValueChange,
            maxSymbols = maxSymbols,
            maxLines = maxLines,
            isError = errorMessageId != null,
            readOnly = readOnly,
            enabled = enabled
        )
        errorMessageId?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp),
                text = stringResource(errorMessageId),
                style = AdminTheme.typography.bodySmall,
                color = AdminTheme.colors.main.error
            )
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun AdminTextFieldPreview() {
    AdminTextField(
        value = "Нужно больше еды \n ...",
        labelStringId = R.string.msg_statistic_day_interval,
        onValueChange = {}
    )
}
