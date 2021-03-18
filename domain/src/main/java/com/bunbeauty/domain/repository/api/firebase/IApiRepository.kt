package com.bunbeauty.domain.repository.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.enums.OrderStatus
import kotlinx.coroutines.flow.SharedFlow

interface IApiRepository {

    val updatedOrderListLiveData: LiveData<List<Order>>

    fun getAddedOrderListLiveData(cafeId: String): LiveData<List<Order>>
    fun updateToken(login: String)
    fun login(login: String, passwordHash: String): LiveData<Boolean>
    fun updateOrder(cafeId: String, uuid: String, newStatus: OrderStatus)
    fun getOrderWithCartProductsList(cafeId: String, daysCount:Int):LiveData<List<Order>>
    fun getCafeList(): SharedFlow<List<Cafe>>
}