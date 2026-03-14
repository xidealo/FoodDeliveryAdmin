package com.bunbeauty.presentation.designsystem.compose.element.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.description_common_navigate
import fooddeliveryadmin.presentation.generated.resources.ic_right_arrow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NavigationTextCard(
    modifier: Modifier = Modifier,
    labelText: String,
    valueText: String? = null,
    isError: Boolean = false,
    errorText: StringResource? = null,
    clickable: Boolean = true,
    elevated: Boolean = true,
    hasDivider: Boolean = false,
    onClick: () -> Unit,
) {
    Column {
        AdminCard(
            modifier = modifier,
            clickable = clickable,
            border =
                if (isError) {
                    BorderStroke(
                        width = 2.dp,
                        color = AdminTheme.colors.main.error,
                    )
                } else {
                    null
                },
            onClick = onClick,
            elevated = elevated,
        ) {
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = 16.dp,
                        vertical =
                            if (valueText == null) {
                                16.dp
                            } else {
                                12.dp
                            },
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = labelText,
                        style =
                            if (valueText == null) {
                                AdminTheme.typography.bodyLarge
                            } else {
                                AdminTheme.typography.labelSmall.medium
                            },
                        color =
                            if (valueText == null) {
                                AdminTheme.colors.main.onSurface
                            } else {
                                AdminTheme.colors.main.onSurfaceVariant
                            },
                    )
                    valueText?.let { text ->
                        Text(
                            text = text,
                            style = AdminTheme.typography.bodyLarge,
                            color = AdminTheme.colors.main.onSurface,
                        )
                    }
                }
                Icon(
                    modifier =
                        Modifier
                            .size(16.dp)
                            .padding(start = 4.dp),
                    painter = painterResource(Res.drawable.ic_right_arrow),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = stringResource(Res.string.description_common_navigate),
                )
            }
        }
        if (isError) {
            errorText?.let {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp),
                    text = stringResource(errorText),
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.error,
                )
            }
        }

        if (hasDivider) {
            AdminHorizontalDivider(
                modifier =
                    Modifier.padding(
                        horizontal = 16.dp,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun TextNavigationCardPreview() {
    AdminTheme {
        NavigationTextCard(
            modifier = Modifier.padding(AdminTheme.dimensions.mediumSpace),
            labelText = "Способ оплаты",
            valueText = "Наличными",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun TextNavigationCardNoHintPreview() {
    AdminTheme {
        NavigationTextCard(
            modifier = Modifier.padding(AdminTheme.dimensions.mediumSpace),
            labelText = "Способ оплаты",
            valueText = null,
            onClick = {},
        )
    }
}
