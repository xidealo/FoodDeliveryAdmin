package com.bunbeauty.fooddeliveryadmin.screen.order_list.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

private val orderStatusShape = RoundedCornerShape(12.dp)

@Composable
fun OrderStatusChip(
    modifier: Modifier = Modifier,
    orderStatus: OrderStatus,
    statusName: String,
) {
    Box(
        modifier = modifier
            .clip(orderStatusShape)
            .background(getOrderColor(orderStatus))
    ) {
        Text(
            text = statusName,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            style = AdminTheme.typography.labelSmall.medium,
            color = AdminTheme.colors.order.onOrder,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyOrderStatusChip(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(orderStatusShape)
            .background(AdminTheme.colors.main.disabled)
    )
}

@Preview
@Composable
private fun StatusChipPreview() {
    AdminTheme {
        OrderStatusChip(
            orderStatus = OrderStatus.ACCEPTED,
            statusName = "Принят"
        )
    }
}

@Preview(heightDp = 24)
@Composable
private fun EmptyOrderStatusChipPreview() {
    AdminTheme {
        EmptyOrderStatusChip(
            modifier = Modifier.width(100.dp)
        )
    }
}
