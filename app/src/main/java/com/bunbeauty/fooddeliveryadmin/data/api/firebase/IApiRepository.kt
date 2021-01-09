package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts

interface IApiRepository {
    fun login(login: String, passwordHash: String): LiveData<Boolean>
    fun updateOrder(order: Order)
    fun getOrderWithCartProductsList():LiveData<List<OrderWithCartProducts>>
    fun getOrderWithCartProducts():LiveData<OrderWithCartProducts>
}