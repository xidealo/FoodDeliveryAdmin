package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.element.switcher.AdminSwitchDefaults
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun SwitcherCard(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    hint: String? = null,
    elevated: Boolean = true,
    enabled: Boolean = true,
    onCheckChanged: (Boolean) -> Unit
) {
    AdminCard(
        modifier = modifier,
        onClick = {
            onCheckChanged(!checked)
        },
        elevated = elevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(end = AdminTheme.dimensions.mediumSpace)
                    .weight(1f),
                verticalArrangement = spacedBy(4.dp)
            ) {
                Text(
                    text = text,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface,
                )
                hint?.let {
                    Text(
                        text = hint,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurfaceVariant,
                    )
                }
            }
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
            text = "Текст",
            hint = "Длинное динное динное динное динное динное описание карточки",
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
            text = "Текст",
            checked = false,
            onCheckChanged = {}
        )
    }
}
