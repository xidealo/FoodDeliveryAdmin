package com.bunbeauty.fooddeliveryadmin.ui.orders

import com.bunbeauty.data.model.order.Order

interface OrdersNavigator {
    fun showChangeStatus(order: Order)
}