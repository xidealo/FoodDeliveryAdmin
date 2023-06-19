package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    orderItem: OrderListUiState.OrderItem,
    onClick: (OrderListUiState.OrderItem) -> Unit,
) {
    AdminCard(
        modifier = modifier,
        onClick = { onClick(orderItem) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = orderItem.code,
                modifier = Modifier
                    .requiredWidthIn(min = AdminTheme.dimensions.codeWidth)
                    .padding(end = AdminTheme.dimensions.smallSpace),
                style = AdminTheme.typography.titleMedium.bold,
                color = AdminTheme.colors.mainColors.onSurface
            )
            OrderStatusChip(orderStatus = orderItem.status, statusName = orderItem.statusString)
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = orderItem.dateTime,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.mainColors.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
                Text(
                    text = orderItem.deferredTime,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.mainColors.onSurface,
                    textAlign = TextAlign.End
                )
            }

        }
    }
}

@Preview
@Composable
private fun OrderItemPreview() {
    AdminTheme {
        OrderItem(
            orderItem = OrderListUiState.OrderItem(
                uuid = "",
                status = OrderStatus.NOT_ACCEPTED,
                statusString = "Обрабатывается",
                code = "Щ-99",
                deferredTime = "",
                dateTime = "9 февраля 22:00"
            ),
            onClick = {},
        )
    }
}

@Preview(fontScale = 1.5f)
@Composable
private fun OrderItemLageFontPreview() {
    AdminTheme {
        OrderItem(
            orderItem = OrderListUiState.OrderItem(
                uuid = "",
                status = OrderStatus.NOT_ACCEPTED,
                statusString = "Обрабатывается",
                code = "Щ-99",
                deferredTime = "Ко времени: 15:00",
                dateTime = "9 февраля 22:00"
            ),
            onClick = {},
        )
    }
}
