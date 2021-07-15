package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.menu_product.ServerMenuProduct
import com.bunbeauty.domain.model.cafe.server.ServerCafe
import com.bunbeauty.domain.model.order.server.ServerOrder
import kotlinx.coroutines.flow.*

interface ApiRepo {

    // LOGIN

    fun login(login: String, passwordHash: String): Flow<Boolean>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    // CAFE

    val cafeList: Flow<List<ServerCafe>>

    // DELIVERY

    val delivery: Flow<Delivery>

    // MENU PRODUCT

    val menuProductList: Flow<List<ServerMenuProduct>>
    fun deleteMenuProductPhoto(photoName: String)
    fun saveMenuProductPhoto(photoByteArray: ByteArray): Flow<String>
    fun saveMenuProduct(menuProduct: ServerMenuProduct)
    fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String)
    fun deleteMenuProduct(uuid: String)

    // ORDER

    fun getAddedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>>
    fun getUpdatedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>>
    val orderList: Flow<List<ServerOrder>>
    fun getOrderListByCafeId(cafeId: String): Flow<List<ServerOrder>>
    fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
}