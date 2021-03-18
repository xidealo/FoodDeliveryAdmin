package com.bunbeauty.presentation.navigator

import com.bunbeauty.data.model.order.Order

interface OrdersNavigator {
    fun showChangeStatus(order: Order)
}