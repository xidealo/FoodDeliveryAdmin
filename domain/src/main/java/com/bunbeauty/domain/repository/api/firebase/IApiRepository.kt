package com.bunbeauty.domain.repository.api.firebase

import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import kotlinx.coroutines.flow.*

interface IApiRepository {

    fun login(login: String, passwordHash: String): Flow<Boolean>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    fun insert(menuProduct: MenuProductFirebase)

    fun updateOrder(order: Order)
    fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String)

    fun delete(order: Order)
    fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>>

    fun getAllOrderList(): Flow<List<Order>>
    fun getOrderListByCafeId(cafeId: String): Flow<List<Order>>

    fun getCafeList(): SharedFlow<List<Cafe>>
    fun getMenuProductList(): Flow<List<MenuProduct>>
}