package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts

interface IApiRepository {
    fun login(login: String, password: String)
    fun updateOrder(order: Order)
    fun getOrderWithCartProducts(login: String):LiveData<List<OrderWithCartProducts>>
}