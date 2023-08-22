package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.element.switcher.AdminSwitchDefaults
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun SwitcherCard(
    modifier: Modifier = Modifier,
    @StringRes labelStringId: Int? = null,
    label: String = "",
    checked: Boolean,
    elevated: Boolean = true,
    enabled: Boolean = true,
    onCheckChanged: (Boolean) -> Unit
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
        elevated = elevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AdminTheme.dimensions.mediumSpace,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val labelText = labelStringId?.let { id ->
                stringResource(id)
            } ?: label
            Text(
                modifier = Modifier
                    .padding(end = AdminTheme.dimensions.mediumSpace)
                    .weight(1f),
                text = labelText,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
                overflow = TextOverflow.Ellipsis
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckChanged,
                colors = AdminSwitchDefaults.switchColors,
                enabled = enabled
            )
        }
    }
}

@Preview
@Composable
private fun SelectedSelectableCard() {
    AdminTheme {
        SwitcherCard(
            label = "Текст",
            checked = true,
            onCheckChanged = {}
        )
    }
}

@Preview
@Composable
private fun UnselectedSelectableCard() {
    AdminTheme {
        SwitcherCard(
            label = "Текст",
            checked = false,
            onCheckChanged = {}
        )
    }
}
