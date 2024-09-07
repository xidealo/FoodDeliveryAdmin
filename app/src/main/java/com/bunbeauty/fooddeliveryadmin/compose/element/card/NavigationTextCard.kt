package com.bunbeauty.fooddeliveryadmin.compose.element.card

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@Composable
fun NavigationTextCard(
    modifier: Modifier = Modifier,
    labelText: String,
    valueText: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    clickable: Boolean = true,
    onClick: () -> Unit
) {
    Column {
        AdminCard(
            modifier = modifier,
            clickable = clickable,
            border = if (isError) {
                BorderStroke(
                    width = 2.dp,
                    color = AdminTheme.colors.main.error
                )
            } else {
                null
            },
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = labelText,
                        style = if (valueText == null) {
                            AdminTheme.typography.bodyLarge
                        } else {
                            AdminTheme.typography.labelSmall.medium
                        },
                        color = if (valueText == null) {
                            AdminTheme.colors.main.onSurface
                        } else {
                            AdminTheme.colors.main.onSurfaceVariant
                        }
                    )
                    valueText?.let { text ->
                        Text(
                            text = text,
                            style = AdminTheme.typography.bodyLarge,
                            color = AdminTheme.colors.main.onSurface
                        )
                    }
                }
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp),
                    painter = painterResource(R.drawable.ic_right_arrow),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = stringResource(R.string.description_common_navigate)
                )
            }
        }
        if (isError) {
            errorText?.let {
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
            onClick = {}
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
            onClick = {}
        )
    }
}
