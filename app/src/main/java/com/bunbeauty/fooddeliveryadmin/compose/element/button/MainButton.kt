package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    @StringRes textStringId: Int? = null,
    text: String? = null,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = AdminButtonDefaults.mainButtonColors,
        shape = AdminButtonDefaults.buttonShape,
        elevation = AdminButtonDefaults.getButtonElevation(elevated),
        enabled = isEnabled
    ) {
        val buttonText = text ?: textStringId?.let {
            stringResource(it)
        } ?: ""
        Text(
            text = buttonText,
            style = AdminTheme.typography.labelLarge.medium,
        )
    }
}

@Preview
@Composable
private fun MainButtonPreview() {
    AdminTheme {
        MainButton(textStringId = R.string.action_retry) {}
    }
}

@Preview
@Composable
private fun MainButtonDisabledPreview() {
    AdminTheme {
        MainButton(
            textStringId = R.string.action_retry,
            isEnabled = false
        ) {}
    }
}
