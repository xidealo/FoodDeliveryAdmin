package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import com.bunbeauty.fooddeliveryadmin.data.model.order.Order

interface IApiRepository {
    fun login(login: String, password: String)
    fun updateOrder(order: Order)
}