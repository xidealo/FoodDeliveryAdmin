package com.bunbeauty.fooddeliveryadmin.compose.element.selectable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.icon16
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun SelectableItem(
    title: String,
    clickable: Boolean,
    elevated: Boolean,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        clickable = clickable,
        elevated = elevated
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = title,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface
            )
            if (isSelected) {
                Icon(
                    modifier = Modifier
                        .padding(start = AdminTheme.dimensions.smallSpace)
                        .icon16()
                        .align(CenterVertically),
                    painter = painterResource(R.drawable.ic_check),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = stringResource(R.string.description_ic_checked)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddressItemPreview() {
    AdminTheme {
        SelectableItem(
            title = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж, код домофона 555",
            clickable = false,
            elevated = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AddressItemSelectedPreview() {
    AdminTheme {
        SelectableItem(
            title = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж, код домофона 555",
            clickable = false,
            elevated = false,
            isSelected = true,
            onClick = {}
        )
    }
}
