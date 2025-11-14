package com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.icon16
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun SelectableItemView(
    modifier: Modifier = Modifier,
    selectableItem: SelectableItem,
    isClickable: Boolean,
    elevated: Boolean,
    hasDivider: Boolean = false,
    onClick: (() -> Unit),
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        clickable = isClickable,
        elevated = elevated,
    ) {
        Column {
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 12.dp),
            ) {
                Text(
                    modifier =
                        Modifier
                            .weight(1f),
                    text = selectableItem.title,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface,
                )
                if (selectableItem.isSelected) {
                    Icon(
                        modifier =
                            Modifier
                                .padding(start = AdminTheme.dimensions.smallSpace)
                                .icon16()
                                .align(Alignment.CenterVertically),
                        painter = painterResource(R.drawable.ic_check),
                        tint = AdminTheme.colors.main.onSurfaceVariant,
                        contentDescription = stringResource(R.string.description_ic_checked),
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
}

@Preview
@Composable
private fun SelectableItemPreview() {
    AdminTheme {
        SelectableItemView(
            selectableItem =
                SelectableItem(
                    uuid = "1",
                    title = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж, код домофона 555",
                    isSelected = false,
                ),
            isClickable = false,
            elevated = false,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SelectableItemSelectedPreview() {
    AdminTheme {
        SelectableItemView(
            selectableItem =
                SelectableItem(
                    uuid = "1",
                    title = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж, код домофона 555",
                    isSelected = false,
                ),
            isClickable = false,
            elevated = false,
            onClick = {},
        )
    }
}
