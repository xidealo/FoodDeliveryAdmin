package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun getOrderColor(orderStatus: OrderStatus): Color {
    return when (orderStatus) {
        OrderStatus.NOT_ACCEPTED -> AdminTheme.colors.orderColors.notAccepted
        OrderStatus.ACCEPTED -> AdminTheme.colors.orderColors.accepted
        OrderStatus.PREPARING -> AdminTheme.colors.orderColors.preparing
        OrderStatus.SENT_OUT -> AdminTheme.colors.orderColors.sentOut
        OrderStatus.DONE -> AdminTheme.colors.orderColors.done
        OrderStatus.DELIVERED -> AdminTheme.colors.orderColors.delivered
        OrderStatus.CANCELED -> AdminTheme.colors.orderColors.canceled
    }
}
