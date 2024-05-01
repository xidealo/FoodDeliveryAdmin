package com.bunbeauty.data

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.ServerList
import com.bunbeauty.data.model.server.addition.AdditionPatchServer
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupPatchServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupServer
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductServer
import com.bunbeauty.data.model.server.nonworkingday.NonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PatchNonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PostNonWorkingDayServer
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
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
    suspend fun getCafeList(cityUuid: String): ApiResult<ServerList<CafeServer>>
    suspend fun patchCafe(
        cafeUuid: String,
        patchCafe: PatchCafeServer,
        token: String
    ): ApiResult<CafeServer>

    // CITY
    suspend fun getCityList(companyUuid: String): ApiResult<ServerList<CityServer>>

    // MENU PRODUCT
    suspend fun getMenuProductList(companyUuid: String): ApiResult<ServerList<MenuProductServer>>
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String>
    suspend fun patchMenuProduct(
        menuProductUuid: String,
        menuProductPatchServer: MenuProductPatchServer,
        token: String
    ): ApiResult<MenuProductServer>

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

    // NON WORKING DAYS
    suspend fun getNonWorkingDaysByCafeUuid(cafeUuid: String): ApiResult<ServerList<NonWorkingDayServer>>
    suspend fun postNonWorkingDay(
        token: String,
        postNonWorkingDay: PostNonWorkingDayServer
    ): ApiResult<NonWorkingDayServer>

    suspend fun patchNonWorkingDay(
        token: String,
        uuid: String,
        patchNonWorkingDay: PatchNonWorkingDayServer
    ): ApiResult<NonWorkingDayServer>

    // ADDITION LIST
    suspend fun getAdditionList(token: String): ApiResult<ServerList<AdditionServer>>
    suspend fun patchAddition(
        additionUuid: String,
        additionPatchServer: AdditionPatchServer,
        token: String
    ): ApiResult<AdditionServer>

    // ADDITION GROUP LIST
    suspend fun getAdditionGroupList(token: String): ApiResult<ServerList<AdditionGroupServer>>
    suspend fun patchAdditionGroup(
        additionGroupUuid: String,
        additionGroupPatchServer: AdditionGroupPatchServer,
        token: String
    ): ApiResult<AdditionGroupServer>
}
