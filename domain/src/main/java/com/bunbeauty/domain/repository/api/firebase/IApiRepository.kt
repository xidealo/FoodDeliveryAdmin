package com.bunbeauty.domain.repository.api.firebase

import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IApiRepository {

    val addedOrderListStateFlow: MutableStateFlow<List<Order>>
    val updatedOrderListStateFlow: MutableStateFlow<List<Order>>

    fun login(login: String, passwordHash: String): Flow<Boolean>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    fun insert(menuProduct: MenuProductFirebase)

    fun updateOrder(order: Order)
    fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String)

    fun delete(order: Order)
    fun subscribeOnOrderList(cafeId: String)

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