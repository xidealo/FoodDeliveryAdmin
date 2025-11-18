package com.bunbeauty.fooddeliveryadmin.screen.orderlist.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun getOrderColor(orderStatus: OrderStatus): Color =
    when (orderStatus) {
        OrderStatus.NOT_ACCEPTED -> AdminTheme.colors.order.notAccepted
        OrderStatus.ACCEPTED -> AdminTheme.colors.order.accepted
        OrderStatus.PREPARING -> AdminTheme.colors.order.preparing
        OrderStatus.SENT_OUT -> AdminTheme.colors.order.sentOut
        OrderStatus.DONE -> AdminTheme.colors.order.done
        OrderStatus.DELIVERED -> AdminTheme.colors.order.delivered
        OrderStatus.CANCELED -> AdminTheme.colors.order.canceled
    }
