package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.firebase.MenuProductFirebase
import com.bunbeauty.domain.model.order.Order
import kotlinx.coroutines.flow.*

interface ApiRepo {

    fun login(login: String, passwordHash: String): Flow<Boolean>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    fun saveMenuProduct(menuProduct: MenuProductFirebase)

    fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
    fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String)

    fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getAllOrderList(): Flow<List<Order>>
    fun getOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getCafeList(): Flow<List<Cafe>>
    fun getMenuProductList(): Flow<List<MenuProduct>>
    fun getDelivery(): Flow<Delivery>
}