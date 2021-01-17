package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus

interface IApiRepository {

    val addedOrderListLiveData: LiveData<List<OrderWithCartProducts>>
    val updatedOrderListLiveData: LiveData<List<OrderWithCartProducts>>

    fun updateToken(login: String)
    fun login(login: String, passwordHash: String): LiveData<Boolean>
    fun updateOrder(uuid: String, newStatus: OrderStatus)
    fun getOrderWithCartProductsList():LiveData<List<OrderWithCartProducts>>
}