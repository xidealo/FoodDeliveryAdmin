package com.bunbeauty.fooddeliveryadmin.screen.cafe_list.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@Composable
fun CafeItem(
    modifier: Modifier = Modifier,
    cafeItem: CafeUiItem,
    onClick: (CafeUiItem) -> Unit,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onClick(cafeItem)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = cafeItem.address,
                modifier = Modifier.fillMaxWidth(),
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface
            )
            Row(modifier = Modifier.padding(top = AdminTheme.dimensions.smallSpace)) {
                Text(
                    text = cafeItem.workingHours,
                    style = AdminTheme.typography.labelMedium.medium,
                    color = AdminTheme.colors.main.onSurfaceVariant,
                )

                Text(
                    modifier = Modifier
                        .padding(start = AdminTheme.dimensions.smallSpace),
                    text = cafeItem.cafeStatusText,
                    style = AdminTheme.typography.labelMedium.medium,
                    color = getCafeStatusColor(cafeItem.cafeStatus),
                )
            }
        }
    }
}

@Composable
private fun getCafeStatusColor(cafeStatus: CafeStatus): Color {
    return when (cafeStatus) {
        CafeStatus.Open -> AdminTheme.colors.status.positive
        CafeStatus.Closed -> AdminTheme.colors.status.negative
        is CafeStatus.CloseSoon -> AdminTheme.colors.status.warning
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CafeItemOpenPreview() {
    AdminTheme {
        CafeItem(
            cafeItem = CafeUiItem(
                uuid = "",
                address = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж",
                workingHours = "9:00 - 22:00",
                cafeStatusText = "Open",
                cafeStatus = CafeStatus.Open,
            ),
            onClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CafeItemCloseSoonPreview() {
    AdminTheme {
        CafeItem(
            cafeItem = CafeUiItem(
                uuid = "",
                address = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж",
                workingHours = "9:00 - 22:00",
                cafeStatusText = "Closed soon 30 min",
                cafeStatus = CafeStatus.CloseSoon(30),
            ),
            onClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CafeItemClosedPreview() {
    AdminTheme {
        CafeItem(
            cafeItem = CafeUiItem(
                uuid = "",
                address = "улица Чапаева, д. 22аб кв. 55, 1 подъезд, 1 этаж",
                workingHours = "9:00 - 22:00",
                cafeStatusText = "Closed",
                cafeStatus = CafeStatus.Closed,
            ),
            onClick = {},
        )
    }
}
