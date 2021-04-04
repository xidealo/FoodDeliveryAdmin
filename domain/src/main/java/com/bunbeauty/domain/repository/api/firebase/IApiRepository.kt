package com.bunbeauty.domain.repository.api.firebase

import androidx.lifecycle.LiveData
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface IApiRepository {

    val updatedOrderListLiveData: LiveData<List<Order>>

    fun updateToken(login: String)
    fun login(login: String, passwordHash: String): SharedFlow<Boolean>

    fun updateOrder(cafeId: String, uuid: String, newStatus: OrderStatus)
    fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String)

    fun getAddedOrderListLiveData(cafeId: String): LiveData<List<Order>>
    fun getOrderWithCartProductsList(
        cafeId: String,
        daysCount: Int
    ): SharedFlow<List<Order>>
    fun getOrderWithCartProductsAllCafesList(
        daysCount: Int
    ): SharedFlow<List<Order>>
    fun getCafeList(): SharedFlow<List<Cafe>>
    fun getMenuProductList(): Flow<List<MenuProduct>>
}