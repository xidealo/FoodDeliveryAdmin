package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.annotation.StringRes
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun SelectableCard(
    modifier: Modifier = Modifier,
    @StringRes labelStringId: Int? = null,
    label: String = "",
    selected: Boolean,
    elevated: Boolean = false,
    onClick: () -> Unit,
) {
    AdminCard(
        modifier = modifier,
        onClick = onClick,
        elevated = elevated,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val labelText =
                labelStringId?.let { id ->
                    stringResource(id)
                } ?: label
            Text(
                modifier =
                    Modifier
                        .padding(end = AdminTheme.dimensions.mediumSpace)
                        .weight(1f),
                text = labelText,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
                overflow = TextOverflow.Ellipsis,
            )
            if (selected) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_check),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = stringResource(R.string.description_common_selected),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectedSelectableCard() {
    AdminTheme {
        SelectableCard(
            label = "Текст",
            selected = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun UnselectedSelectableCard() {
    AdminTheme {
        SelectableCard(
            label = "Текст",
            selected = false,
            onClick = {},
        )
    }
}
