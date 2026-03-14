package com.bunbeauty.presentation.designsystem.compose.element.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.rememberMultipleEventsCutter
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_retry
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStringId: StringResource? = null,
    text: String? = null,
    colors: ButtonColors = AdminButtonDefaults.mainButtonColors,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp,
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
            enabled = isEnabled,
        ) {
            val buttonText =
                text ?: textStringId?.let {
                    stringResource(it)
                } ?: ""
            Text(
                text = buttonText,
                style = AdminTheme.typography.labelLarge.medium,
                color = AdminTheme.colors.main.onPrimary,
            )
        }
    }
}

@Preview
@Composable
private fun MainButtonPreview() {
    AdminTheme {
        MainButton(
            textStringId = Res.string.action_retry,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun MainButtonDisabledPreview() {
    AdminTheme {
        MainButton(
            textStringId = Res.string.action_retry,
            isEnabled = false,
            onClick = {},
        )
    }
}
