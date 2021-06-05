package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.ServerMenuProduct
import com.bunbeauty.domain.model.cafe.server.ServerCafe
import com.bunbeauty.domain.model.order.server.ServerOrder
import kotlinx.coroutines.flow.*

interface ApiRepo {

    // LOGIN

    fun login(login: String, passwordHash: String): Flow<Boolean>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    // CAFE

    fun getCafeList(): Flow<List<ServerCafe>>

    // DELIVERY

    fun getDelivery(): Flow<Delivery>

    // MENU PRODUCT

    val menuProductList: Flow<List<ServerMenuProduct>>
    fun saveMenuProductPhoto(photoByteArray: ByteArray): Flow<String>
    fun saveMenuProduct(menuProduct: ServerMenuProduct, uuid: String)

    // ORDER

    fun getAddedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>>
    fun getUpdatedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>>
    fun getAllOrderList(): Flow<List<ServerOrder>>
    fun getOrderListByCafeId(cafeId: String): Flow<List<ServerOrder>>
    fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
}