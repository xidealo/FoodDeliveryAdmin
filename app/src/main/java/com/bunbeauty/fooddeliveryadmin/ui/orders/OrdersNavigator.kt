package com.bunbeauty.fooddeliveryadmin.ui.orders

import com.bunbeauty.fooddeliveryadmin.data.model.order.Order

interface OrdersNavigator {
    fun showChangeStatus(order: Order)
}