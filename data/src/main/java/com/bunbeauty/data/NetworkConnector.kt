package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.data.model.server.ServerMenuProduct
import com.bunbeauty.data.model.server.cafe.ServerCafe
import com.bunbeauty.data.model.server.order.ServerOrder
import kotlinx.coroutines.flow.*

interface NetworkConnector {

    // LOGIN

    fun login(login: String, passwordHash: String): ApiResult<String>
    fun subscribeOnNotification()
    fun unsubscribeOnNotification()

    // CAFE

    fun getCafeList(): ApiResult<ListServer<ServerCafe>>

    // DELIVERY

    fun getDelivery(): ApiResult<Delivery>

    // MENU PRODUCT

    fun getMenuProductList(): ApiResult<ListServer<ServerMenuProduct>>
    fun deleteMenuProductPhoto(photoName: String)
    fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>
    fun saveMenuProduct(menuProduct: ServerMenuProduct)
    fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String)
    fun deleteMenuProduct(uuid: String)

    // ORDER

    fun getAddedOrderListByCafeId(cafeUuid: String): ApiResult<ListServer<ServerOrder>>
    fun getUpdatedOrderListByCafeId(cafeUuid: String): ApiResult<ListServer<ServerOrder>>
    fun getOrderList(): ApiResult<ListServer<ServerOrder>>
    fun getOrderListByCafeId(cafeId: String): ApiResult<ListServer<ServerOrder>>
    fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
}