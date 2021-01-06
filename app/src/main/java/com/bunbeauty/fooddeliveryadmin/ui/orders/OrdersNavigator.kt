package com.bunbeauty.fooddeliveryadmin.ui.orders

import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts

interface OrdersNavigator {
    fun showChangeStatus(orderWithCartProducts: OrderWithCartProducts)
}