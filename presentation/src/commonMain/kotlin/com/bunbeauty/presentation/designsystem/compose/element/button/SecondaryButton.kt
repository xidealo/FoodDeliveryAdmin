package com.bunbeauty.presentation.designsystem.compose.element.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.rememberMultipleEventsCutter
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_cancel
import fooddeliveryadmin.presentation.generated.resources.ic_add_photo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
     textStringId: StringResource,
    onClick: () -> Unit,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    icon: DrawableResource? = null,
    borderColor: Color = AdminTheme.colors.main.onSecondary,
    buttonColors: ButtonColors = AdminButtonDefaults.neutralSecondaryButtonColors,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        val multipleEventsCutter = rememberMultipleEventsCutter()
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                multipleEventsCutter.processEvent(onClick)
            },
            colors =
                if (isError) {
                    AdminButtonDefaults.errorSecondaryButtonColors
                } else {
                    buttonColors
                },
            border =
                BorderStroke(
                    width = 2.dp,
                    color =
                        if (isError) {
                            AdminTheme.colors.main.error
                        } else {
                            borderColor
                        },
                ),
            shape = AdminButtonDefaults.buttonShape,
            elevation = AdminButtonDefaults.getButtonElevation(elevated),
            enabled = isEnabled,
        ) {
            icon?.let { icon ->
                Icon(
                    modifier =
                        Modifier
                            .padding(end = 8.dp),
                    painter = painterResource(icon),
                    contentDescription = null,
                )
            }
            Text(
                text = stringResource(textStringId),
                style = AdminTheme.typography.labelLarge.medium,
            )
        }
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    AdminTheme {
        SecondaryButton(
            textStringId = Res.string.action_common_cancel,
            onClick = { },
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonWithIconPreview() {
    AdminTheme {
        SecondaryButton(
            textStringId = Res.string.action_common_cancel,
            onClick = { },
            icon = Res.drawable.ic_add_photo,
            borderColor = AdminTheme.colors.main.primary,
            buttonColors = AdminButtonDefaults.accentSecondaryButtonColors,
        )
    }
}
