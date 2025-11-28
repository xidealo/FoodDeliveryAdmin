package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.WEB_SOCKET_TAG
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.ServerList
import com.bunbeauty.data.model.server.addition.AdditionPatchServer
import com.bunbeauty.data.model.server.addition.AdditionPostServer
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupPatchServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupPostServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupServer
import com.bunbeauty.data.model.server.additiongroup.PatchMenuProductToAdditionGroupPriorityUuid
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.data.model.server.category.CategoryPatchServer
import com.bunbeauty.data.model.server.category.CategoryServer
import com.bunbeauty.data.model.server.category.CreateCategoryPostServer
import com.bunbeauty.data.model.server.category.PatchCategoryList
import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.data.model.server.company.CompanyPatchServer
import com.bunbeauty.data.model.server.company.WorkInfoData
import com.bunbeauty.data.model.server.menuProductToAdditionGroup.MenuProductToAdditionGroupServer
import com.bunbeauty.data.model.server.menuProductToAdditionGroupToAddition.MenuProductToAdditionGroupToAdditionServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductAdditionsPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductAdditionsPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductServer
import com.bunbeauty.data.model.server.nonworkingday.NonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PatchNonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PostNonWorkingDayServer
import com.bunbeauty.data.model.server.order.OrderAvailabilityServer
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.data.model.server.request.UpdateUnlimitedNotificationRequest
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.data.model.server.user.UserAuthorizationResponse
import com.bunbeauty.data.model.server.user.UserResponse
import com.bunbeauty.domain.enums.OrderStatus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpMethod
import io.ktor.http.path
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.SocketException

class FoodDeliveryApiImpl(
    private val client: HttpClient,
    private val json: Json,
) : FoodDeliveryApi {
    private var webSocketSession: DefaultClientWebSocketSession? = null

    private val mutableUpdatedOrderFlow = MutableSharedFlow<ApiResult<OrderServer>>()

    private val mutex = Mutex()

    private var webSocketSessionOpened = false

    override suspend fun login(userAuthorizationRequest: UserAuthorizationRequest): ApiResult<UserAuthorizationResponse> =
        post(
            path = "user/login",
            body = userAuthorizationRequest,
        )

    override suspend fun getUser(token: String): ApiResult<UserResponse> =
        get(
            path = "user",
            token = token,
        )

    override suspend fun putNotificationToken(
        updateNotificationTokenRequest: UpdateNotificationTokenRequest,
        token: String,
    ): ApiResult<Unit> =
        put(
            path = "user/notification_token",
            body = updateNotificationTokenRequest,
            token = token,
        )

    override suspend fun deleteNotificationToken(token: String): ApiResult<Unit> =
        delete(
            path = "user/notification_token",
            token = token,
        )

    override suspend fun putUnlimitedNotification(
        updateUnlimitedNotificationRequest: UpdateUnlimitedNotificationRequest,
        token: String,
    ): ApiResult<Unit> =
        put(
            path = "user/unlimited_notification",
            body = updateUnlimitedNotificationRequest,
            token = token,
        )

    override suspend fun getCafeList(cityUuid: String): ApiResult<ServerList<CafeServer>> =
        get(
            path = "cafe",
            parameters = listOf("cityUuid" to cityUuid),
        )

    override suspend fun getCafeByUuid(cafeUuid: String): ApiResult<CafeServer> =
        get(
            path = "v2/cafe",
            parameters = listOf("cafeUuid" to cafeUuid),
        )

    override suspend fun patchCafe(
        cafeUuid: String,
        patchCafe: PatchCafeServer,
        token: String,
    ): ApiResult<CafeServer> =
        patch(
            path = "cafe",
            parameters = listOf("cafeUuid" to cafeUuid),
            body = patchCafe,
            token = token,
        )

    override suspend fun getCityList(companyUuid: String): ApiResult<ServerList<CityServer>> =
        get(
            path = "city",
            parameters = listOf("companyUuid" to companyUuid),
        )

    override suspend fun getMenuProductList(companyUuid: String): ApiResult<ServerList<MenuProductServer>> =
        get(
            path = "menu_product",
            parameters = listOf("companyUuid" to companyUuid),
        )

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String> = ApiResult.Success(":")

    override suspend fun patchMenuProduct(
        menuProductUuid: String,
        menuProductPatchServer: MenuProductPatchServer,
        token: String,
    ): ApiResult<MenuProductServer> =
        patch(
            path = "menu_product",
            body = menuProductPatchServer,
            parameters = listOf("uuid" to menuProductUuid),
            token = token,
        )

    override suspend fun patchMenuProductAdditions(
        token: String,
        menuProductToAdditionGroupUuid: String,
        menuProductAdditionsPatchServer: MenuProductAdditionsPatchServer,
    ): ApiResult<Unit> =
        patch(
            path = "menu_product/addition_group_with_additions",
            body = menuProductAdditionsPatchServer,
            parameters = listOf("uuid" to menuProductToAdditionGroupUuid),
            token = token,
        )

    override suspend fun postMenuProduct(
        token: String,
        menuProductPostServer: MenuProductPostServer,
    ): ApiResult<MenuProductServer> =
        post<MenuProductServer>(
            path = "menu_product",
            body = menuProductPostServer,
            token = token,
        )

    override suspend fun postMenuProductAdditions(
        token: String,
        menuProductAdditionsPostServer: MenuProductAdditionsPostServer,
    ): ApiResult<List<MenuProductServer>> =
        post<List<MenuProductServer>>(
            path = "addition_group_to_menu_products",
            body = menuProductAdditionsPostServer,
            token = token,
        )

    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<StatisticServer> {
        // TODO refactor
        return client
            .get {
                url {
                    path("statistic")
                }
                parameter("cafeUuid", cafeUuid)
                parameter("period", period)

                header("Authorization", "Bearer $token")
            }.body()
    }

    override suspend fun getUpdatedOrderFlowByCafeUuid(
        token: String,
        cafeUuid: String,
    ): Flow<ApiResult<OrderServer>> {
        mutex.withLock {
            if (!webSocketSessionOpened) {
                subscribeOnOrderUpdates(token, cafeUuid)
            }
        }
        return mutableUpdatedOrderFlow.asSharedFlow()
    }

    private fun subscribeOnOrderUpdates(
        token: String,
        cafeUuid: String,
    ) {
        CoroutineScope(Job() + IO).launch {
            try {
                webSocketSessionOpened = true
                client.wss(
                    HttpMethod.Get,
                    path = "user/order/subscribe",
                    port = 443,
                    request = {
                        header("Authorization", "Bearer $token")
                        parameter("cafeUuid", cafeUuid)
                    },
                ) {
                    Log.d(WEB_SOCKET_TAG, "WebSocket connected")
                    webSocketSession = this
                    while (true) {
                        val message = incoming.receive() as? Frame.Text ?: continue
                        Log.d(WEB_SOCKET_TAG, "Message: ${message.readText()}")
                        val serverModel =
                            json.decodeFromString(OrderServer.serializer(), message.readText())
                        mutableUpdatedOrderFlow.emit(ApiResult.Success(serverModel))
                    }
                }
            } catch (exception: WebSocketException) {
                Log.e(WEB_SOCKET_TAG, "WebSocketException: ${exception.message}")
                mutableUpdatedOrderFlow.emit(ApiResult.Error(ApiError(message = exception.message.toString())))
            } catch (exception: SocketException) {
                Log.e(WEB_SOCKET_TAG, "SocketException: ${exception.message}")
                mutableUpdatedOrderFlow.emit(ApiResult.Error(ApiError(message = exception.message.toString())))
            } catch (exception: ClosedReceiveChannelException) {
                Log.d(WEB_SOCKET_TAG, "ClosedReceiveChannelException: ${exception.message}")
                // Nothing
            } catch (exception: Exception) {
                val stackTrace =
                    exception.stackTrace.joinToString("\n") {
                        "${it.className} ${it.methodName} ${it.lineNumber}"
                    }
                Log.e(WEB_SOCKET_TAG, "Exception: $exception \n$stackTrace")
                mutableUpdatedOrderFlow.emit(ApiResult.Error(ApiError(message = exception.message.toString())))
            } finally {
                webSocketSessionOpened = false
            }
        }
    }

    override suspend fun unsubscribeOnOrderList(message: String) {
        if (webSocketSession != null) {
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, message))
            webSocketSession = null

            Log.d(WEB_SOCKET_TAG, "webSocketSession closed ($message)")
        }
    }

    override suspend fun getOrderListByCafeUuid(
        token: String,
        cafeUuid: String,
    ): ApiResult<ServerList<OrderServer>> =
        get(
            path = "order",
            parameters = listOf("cafeUuid" to cafeUuid),
            token = token,
        )

    override suspend fun getOrderByUuid(
        token: String,
        orderUuid: String,
    ): ApiResult<OrderDetailsServer> =
        get(
            path = "v2/order/details",
            parameters = listOf("uuid" to orderUuid),
            token = token,
        )

    override suspend fun getOrderAvailability(companyUuid: String): ApiResult<OrderAvailabilityServer> =
        get(
            path = "order_availability",
            parameters = listOf("companyUuid" to companyUuid),
        )

    override suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus,
    ): ApiResult<OrderDetailsServer> =
        patch(
            path = "order",
            body = mapOf("status" to status.toString()),
            parameters = listOf("uuid" to orderUuid),
            token = token,
        )

    override suspend fun getWorkInfo(companyUuid: String): ApiResult<WorkInfoData> =
        get(
            path = "work_info",
            parameters = listOf("companyUuid" to companyUuid),
        )

    override suspend fun patchCompany(
        token: String,
        companyPatch: CompanyPatchServer,
        companyUuid: String,
    ): ApiResult<Unit> =
        patch(
            path = "company",
            body = companyPatch,
            parameters = listOf("companyUuid" to companyUuid),
            token = token,
        )

    override suspend fun getCategoriesByCompanyUuid(
        token: String,
        companyUuid: String,
    ): ApiResult<ServerList<CategoryServer>> =
        get(
            path = "category",
            parameters = listOf("companyUuid" to companyUuid),
            token = token,
        )

    override suspend fun postCategory(
        token: String,
        categoryServerPost: CreateCategoryPostServer,
    ): ApiResult<CategoryServer> =
        post(
            path = "category",
            body = categoryServerPost,
            token = token,
        )

    override suspend fun patchCategory(
        token: String,
        uuid: String,
        patchCategory: CategoryPatchServer,
    ): ApiResult<CategoryServer> =
        patch(
            path = "category",
            body = patchCategory,
            parameters = listOf("uuid" to uuid),
            token = token,
        )

    override suspend fun patchCategoryPriority(
        token: String,
        patchCategoryItem: PatchCategoryList,
    ): ApiResult<Unit> =
        patch(
            path = "category/list",
            body = patchCategoryItem,
            token = token,
        )

    override suspend fun getNonWorkingDaysByCafeUuid(cafeUuid: String): ApiResult<ServerList<NonWorkingDayServer>> =
        get(
            path = "non_working_day",
            parameters = listOf("cafeUuid" to cafeUuid),
        )

    override suspend fun postNonWorkingDay(
        token: String,
        postNonWorkingDay: PostNonWorkingDayServer,
    ): ApiResult<NonWorkingDayServer> =
        post(
            path = "non_working_day",
            body = postNonWorkingDay,
            token = token,
        )

    override suspend fun patchNonWorkingDay(
        token: String,
        uuid: String,
        patchNonWorkingDay: PatchNonWorkingDayServer,
    ): ApiResult<NonWorkingDayServer> =
        patch(
            path = "non_working_day",
            body = patchNonWorkingDay,
            parameters = listOf("uuid" to uuid),
            token = token,
        )

    override suspend fun getAdditionList(token: String): ApiResult<ServerList<AdditionServer>> =
        get(
            path = "addition",
            token = token,
        )

    override suspend fun getAdditionGroupList(token: String): ApiResult<ServerList<AdditionGroupServer>> =
        get(
            path = "addition_group",
            token = token,
        )

    override suspend fun patchAddition(
        additionUuid: String,
        additionPatchServer: AdditionPatchServer,
        token: String,
    ): ApiResult<AdditionServer> =
        patch(
            path = "addition",
            body = additionPatchServer,
            parameters = listOf("uuid" to additionUuid),
            token = token,
        )

    override suspend fun postAddition(
        additionPostServer: AdditionPostServer,
        token: String,
    ): ApiResult<AdditionServer> =
        post(
            path = "addition",
            body = additionPostServer,
            token = token,
        )

    override suspend fun patchAdditionGroup(
        additionGroupUuid: String,
        additionGroupPatchServer: AdditionGroupPatchServer,
        token: String,
    ): ApiResult<AdditionGroupServer> =
        patch(
            path = "addition_group",
            body = additionGroupPatchServer,
            parameters = listOf("uuid" to additionGroupUuid),
            token = token,
        )

    override suspend fun postAdditionGroup(
        token: String,
        additionGroupServerPost: AdditionGroupPostServer,
    ): ApiResult<AdditionGroupServer> =
        post(
            path = "addition_group",
            body = additionGroupServerPost,
            token = token,
        )

    override suspend fun getMenuProductToAdditionGroup(
        token: String,
        uuid: String,
    ): ApiResult<MenuProductToAdditionGroupServer> =
        get(
            path = "menu_product_to_addition_group",
            token = token,
            parameters = listOf("uuid" to uuid),
        )

    override suspend fun getMenuProductToAdditionGroupToAdditionList(
        token: String,
        uuidList: List<String>,
    ): ApiResult<List<MenuProductToAdditionGroupToAdditionServer>> =
        get(
            path = "menu_product_to_addition_group_to_addition",
            token = token,
            parameters = uuidList.map { uuid -> "uuidList" to uuid },
        )

    override suspend fun patchMenuProductToAdditionGroupPriorityUuid(
        token: String,
        additionGroupListUuid: PatchMenuProductToAdditionGroupPriorityUuid,
    ): ApiResult<Unit> =
        patch(
            path = "menu_product_to_addition_group/priority_list",
            token = token,
            body = additionGroupListUuid,
        )

    private suspend inline fun <reified T> get(
        path: String,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ): ApiResult<T> =
        safeCall {
            client.get {
                buildRequest(
                    path = path,
                    body = null,
                    parameters = parameters,
                    token = token,
                )
            }
        }

    private suspend inline fun <reified T> post(
        path: String,
        body: Any,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ): ApiResult<T> =
        safeCall {
            client.post {
                buildRequest(
                    path = path,
                    body = body,
                    parameters = parameters,
                    token = token,
                )
            }
        }

    private suspend inline fun <reified T> put(
        path: String,
        body: Any,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ): ApiResult<T> =
        safeCall {
            client.put {
                buildRequest(
                    path = path,
                    body = body,
                    parameters = parameters,
                    token = token,
                )
            }
        }

    private suspend inline fun <reified T> patch(
        path: String,
        body: Any,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ): ApiResult<T> =
        safeCall {
            client.patch {
                buildRequest(
                    path = path,
                    body = body,
                    parameters = parameters,
                    token = token,
                )
            }
        }

    private suspend inline fun <reified T> delete(
        path: String,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ): ApiResult<T> =
        safeCall {
            client.delete {
                buildRequest(
                    path = path,
                    body = null,
                    parameters = parameters,
                    token = token,
                )
            }
        }

    private fun HttpRequestBuilder.buildRequest(
        path: String,
        body: Any?,
        parameters: List<Pair<String, String?>> = listOf(),
        token: String? = null,
    ) {
        if (body != null) {
            setBody(body)
        }
        url {
            path(path)
        }
        parameters.forEach { parameterMap ->
            parameter(parameterMap.first, parameterMap.second)
        }
        header(Authorization, "Bearer $token")
    }

    private suspend inline fun <reified R> safeCall(crossinline networkCall: suspend () -> HttpResponse): ApiResult<R> =
        try {
            withContext(IO) {
                ApiResult.Success(networkCall().body())
            }
        } catch (exception: ResponseException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message ?: ""))
        } catch (exception: Throwable) {
            ApiResult.Error(ApiError(0, exception.message ?: "Bad Internet"))
        }
}
