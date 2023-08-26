package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.ServerList
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.enums.OrderStatus
import kotlinx.coroutines.flow.Flow

interface FoodDeliveryApi {

    // LOGIN
    suspend fun login(
        userAuthorizationRequest: UserAuthorizationRequest
    ): ApiResult<UserAuthorizationResponse>

    // CAFE
    suspend fun getCafeList(token: String, cityUuid: String): ApiResult<ServerList<CafeServer>>

    // MENU PRODUCT
    suspend fun getMenuProductList(companyUuid: String): ServerList<MenuProductServer>
    suspend fun deleteMenuProductPhoto(photoName: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>
    suspend fun updateVisibleMenuProductUseCase(uuid: String, isVisible: Boolean, token: String)
    suspend fun patchMenuProduct(menuProductServer: MenuProductServer, token: String)
    suspend fun deleteMenuProduct(uuid: String)

    // STATISTIC
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): List<StatisticServer>

    // ORDER

    suspend fun getUpdatedOrderFlowByCafeUuid(
        token: String,
        cafeUuid: String
    ): Flow<ApiResult<OrderServer>>

    suspend fun unsubscribeOnOrderList(message: String)

    suspend fun getOrderListByCafeUuid(
        token: String,
        cafeUuid: String
    ): ApiResult<ServerList<OrderServer>>

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
    ): ApiResult<ServerList<CategoryServer>>
}