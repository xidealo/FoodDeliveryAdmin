package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.enums.OrderStatus
import kotlinx.coroutines.flow.Flow

interface FoodDeliveryApi {

    // LOGIN
    suspend fun login(userAuthorizationRequest: UserAuthorizationRequest): ApiResult<UserAuthorizationResponse>

    // CAFE
    suspend fun getCafeList(token: String, cityUuid: String): ApiResult<ListServer<CafeServer>>

    // MENU PRODUCT
    suspend fun getMenuProductList(companyUuid: String): ListServer<MenuProductServer>
    suspend fun deleteMenuProductPhoto(photoName: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>

    suspend fun updateVisibleMenuProductUseCase(uuid: String, isVisible: Boolean, token: String)
    suspend fun deleteMenuProduct(uuid: String)

    // STATISTIC
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): List<StatisticServer>

    // ORDER

    suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeUuid: String
    ): Flow<ApiResult<ServerOrder>>

    suspend fun unsubscribeOnOrderList(message: String)

    suspend fun getOrderListByCafeId(
        token: String,
        cafeUuid: String
    ): ApiResult<ListServer<ServerOrder>>

    suspend fun getOrderByUuid(token: String, orderUuid: String): ApiResult<OrderDetailsServer>

    suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus
    ): ApiResult<OrderDetailsServer>

    // CATEGORIES
    suspend fun getCategoriesByCompanyUuid(
        token: String,
        companyUuid: String
    ): ApiResult<ListServer<CategoryServer>>
}