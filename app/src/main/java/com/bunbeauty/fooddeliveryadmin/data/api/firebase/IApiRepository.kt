package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus

interface IApiRepository {

    val addedOrderListLiveData: LiveData<List<Order>>
    val updatedOrderListLiveData: LiveData<List<Order>>

    fun updateToken(login: String)
    fun login(login: String, passwordHash: String): LiveData<Boolean>
    fun updateOrder(uuid: String, newStatus: OrderStatus)
    fun getOrderWithCartProductsList(daysCount:Int):LiveData<List<Order>>
}