package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.DeliveryServer
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.enums.OrderStatus
import kotlinx.coroutines.flow.Flow

interface NetworkConnector {

    // LOGIN
    suspend fun login(userAuthorizationRequest: UserAuthorizationRequest): ApiResult<UserAuthorizationResponse>

    // CAFE
    suspend fun getCafeList(token: String, cityUuid: String): ApiResult<ListServer<CafeServer>>

    // DELIVERY
    suspend fun getDelivery(token: String, companyUuid: String): ApiResult<DeliveryServer>

    // MENU PRODUCT
    suspend fun getMenuProductList(companyUuid: String): ApiResult<ListServer<MenuProductServer>>
    suspend fun deleteMenuProductPhoto(photoName: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>

    //  suspend fun saveMenuProduct(menuProduct: ServerMenuProduct)
    //  suspend fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String)
    suspend fun deleteMenuProduct(uuid: String)

    // STATISTIC
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): ApiResult<ListServer<StatisticServer>>

    // ORDER

    suspend fun subscribeOnNotification(cafeId: String)

    suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeId: String
    ): Flow<ApiResult<ServerOrder>>

    suspend fun unsubscribeOnNotification(cafeId: String)

    suspend fun unsubscribeOnOrderList(cafeId: String, message: String)

    suspend fun getOrderListByCafeId(
        token: String,
        cafeId: String
    ): ApiResult<ListServer<ServerOrder>>

    suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus
    ): ApiResult<ServerOrder>

    // CATEGORIES
    suspend fun getCategoriesByCompanyUuid(
        token: String,
        companyUuid: String
    ): ApiResult<ListServer<CategoryServer>>
}