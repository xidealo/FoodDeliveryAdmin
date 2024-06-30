package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    label: String?,
    @StringRes hintStringId: Int? = null,
    clickable: Boolean = true,
    border: BorderStroke? = null,
    onClick: () -> Unit
) {
    AdminCard(
        modifier = modifier,
        clickable = clickable,
        border = border,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = AdminTheme.dimensions.mediumSpace,
                vertical = if (hintStringId == null) {
                    12.dp
                } else {
                    8.dp
                }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = AdminTheme.dimensions.smallSpace)
            ) {
                hintStringId?.let {
                    Text(
                        text = stringResource(hintStringId),
                        style = AdminTheme.typography.labelSmall.medium,
                        color = AdminTheme.colors.main.onSurfaceVariant
                    )
                }
                Text(
                    text = label ?: "",
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )
            }
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_right_arrow),
                tint = AdminTheme.colors.main.onSurfaceVariant,
                contentDescription = stringResource(R.string.description_common_navigate)
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
            hintStringId = R.string.hint_login_login,
            label = "+7 999 000-00-00",
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
            hintStringId = null,
            label = "+7 999 000-00-00",
            onClick = {}
        )
    }
}
