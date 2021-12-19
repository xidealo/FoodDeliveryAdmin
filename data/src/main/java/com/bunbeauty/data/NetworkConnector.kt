package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.data.model.server.ServerMenuProduct
import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.data.model.server.UserAuthorization
import com.bunbeauty.data.model.server.cafe.ServerCafe
import com.bunbeauty.data.model.server.order.ServerOrder
import kotlinx.coroutines.flow.*

interface NetworkConnector {

    // LOGIN
    suspend fun login(userAuthorization: UserAuthorization): ApiResult<UserAuthorization>
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

    // STATISTIC
    suspend fun getStatistic(period: String): ApiResult<ListServer<StatisticServer>>

    // ORDER
    suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeId: String
    ): Flow<ApiResult<ServerOrder>>

    suspend fun getOrderListByCafeId(
        token: String,
        cafeId: String
    ): ApiResult<ListServer<ServerOrder>>

    suspend fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus)
}