package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.rememberMultipleEventsCutter
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    @StringRes textStringId: Int? = null,
    text: String? = null,
    colors: ButtonColors = AdminButtonDefaults.mainButtonColors,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false
    ) {
        val multipleEventsCutter = rememberMultipleEventsCutter()
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                multipleEventsCutter.processEvent(onClick)
            },
            colors = colors,
            shape = AdminButtonDefaults.buttonShape,
            elevation = AdminButtonDefaults.getButtonElevation(elevated),
            enabled = isEnabled
        ) {
            val buttonText = text ?: textStringId?.let {
                stringResource(it)
            } ?: ""
            Text(
                text = buttonText,
                style = AdminTheme.typography.labelLarge.medium
            )
        }
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
