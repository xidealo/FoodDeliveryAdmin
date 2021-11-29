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

    suspend fun login(login: String, passwordHash: String): ApiResult<String>
    suspend fun subscribeOnNotification()
    suspend fun unsubscribeOnNotification()

    // CAFE

    suspend fun getCafeList(): ApiResult<ListServer<ServerCafe>>

    // DELIVERY

    suspend fun getDelivery(): ApiResult<Delivery>

    // MENU PRODUCT

    suspend fun getMenuProductList(): ApiResult<ListServer<ServerMenuProduct>>
    suspend fun deleteMenuProductPhoto(photoName: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>
    suspend fun saveMenuProduct(menuProduct: ServerMenuProduct)
    suspend fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String)
    suspend fun deleteMenuProduct(uuid: String)

    // ORDER

    suspend fun getAddedOrderListByCafeId(cafeUuid: String): ApiResult<ListServer<ServerOrder>>
    suspend fun getUpdatedOrderListByCafeId(cafeUuid: String): ApiResult<ListServer<ServerOrder>>
    suspend fun getOrderList(): ApiResult<ListServer<ServerOrder>>
    suspend fun getOrderListByCafeId(cafeId: String): ApiResult<ListServer<ServerOrder>>
    suspend fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
}