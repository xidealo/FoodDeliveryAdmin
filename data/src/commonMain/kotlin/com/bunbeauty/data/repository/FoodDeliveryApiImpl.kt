package com.bunbeauty.data.repository
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
import com.bunbeauty.data.model.server.cafe.GetDeliveryZoneResponse
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.data.model.server.cafe.PatchDeliveryZone
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
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.data.model.server.request.UpdateUnlimitedNotificationRequest
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.statistic.StatisticDayDetailServer
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.data.model.server.user.UserAuthorizationResponse
import com.bunbeauty.data.model.server.user.UserResponse
import com.bunbeauty.domain.enums.OrderStatus
import common.ApiError
import common.ApiResult
import common.Constants.WEB_SOCKET_TAG
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
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

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

    override suspend fun getDeliveryZone(
        cafeUuid: String,
        token: String,
    ): ApiResult<GetDeliveryZoneResponse> =
        get(
            token = token,
            path = "delivery_zone",
            parameters = listOf("cafeUuid" to cafeUuid),
        )

    override suspend fun patchDeliveryZone(
        cafeUuid: String,
        zoneUuid: String,
        token: String,
        patchZone: PatchDeliveryZone,
    ): ApiResult<Unit> =
        patch(
            path = "delivery_zone",
            body = patchZone,
            parameters =
                listOf(
                    "cafeUuid" to cafeUuid,
                    "uuid" to zoneUuid,
                ),
            token = token,
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

    override suspend fun getStatisticDayDetail(
        token: String,
        companyUuid: String,
        date: String,
    ): StatisticDayDetailServer =
        when (
            val result =
                get<StatisticDayDetailServer>(
                    path = "statistic/day-detail",
                    token = token,
                    parameters =
                        listOf(
                            "companyUuid" to companyUuid,
                            "date" to date,
                        ),
                )
        ) {
            is ApiResult.Success -> result.data
            is ApiResult.Error ->
                throw IllegalStateException(result.apiError.message)
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
        CoroutineScope(SupervisorJob() + Default).launch {
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
                    println("$WEB_SOCKET_TAG: WebSocket connected")
                    webSocketSession = this
                    while (isActive) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                println("$WEB_SOCKET_TAG: Frame.Text (${text.length} chars): $text")
                                val serverModel =
                                    json.decodeFromString(
                                        OrderServer.serializer(),
                                        text,
                                    )
                                mutableUpdatedOrderFlow.emit(ApiResult.Success(serverModel))
                            }
                            is Frame.Binary -> {
                                println(
                                    "$WEB_SOCKET_TAG: Frame.Binary (${frame.data.size} bytes): ${bytesPreview(frame.data)}",
                                )
                            }
                            is Frame.Ping -> send(Frame.Pong(frame.data))

                            is Frame.Pong -> {
                                println(
                                    "$WEB_SOCKET_TAG: Frame.Pong (${frame.data.size} bytes): ${bytesPreview(frame.data)}",
                                )
                            }
                            is Frame.Close -> {
                                val reason = frame.readReason()
                                println("$WEB_SOCKET_TAG: Frame.Close reason=$reason")
                                break
                            }

                            else -> {
                                println("$WEB_SOCKET_TAG else")
                            }
                        }
                    }
                }
            } catch (exception: WebSocketException) {
                logWsException("WebSocketException", exception)
                mutableUpdatedOrderFlow.emit(
                    ApiResult.Error(ApiError(message = describeException(exception))),
                )
            } catch (exception: ClosedReceiveChannelException) {
                logWsException("ClosedReceiveChannelException", exception)
            } catch (exception: Exception) {
                logWsException("Exception", exception)
                mutableUpdatedOrderFlow.emit(
                    ApiResult.Error(ApiError(message = describeException(exception))),
                )
            } finally {
                webSocketSessionOpened = false
            }
        }
    }

    override suspend fun unsubscribeOnOrderList(message: String) {
        if (webSocketSession != null) {
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, message))
            webSocketSession = null

            println("$WEB_SOCKET_TAG: webSocketSession closed ($message)")
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
            withContext(Default) {
                ApiResult.Success(networkCall().body())
            }
        } catch (exception: ResponseException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message ?: ""))
        } catch (exception: Throwable) {
            ApiResult.Error(ApiError(0, exception.message ?: "Bad Internet"))
        }

    private fun logWsException(
        label: String,
        throwable: Throwable,
    ) {
        println(
            "$WEB_SOCKET_TAG: $label -> ${describeException(throwable)}\n" +
                throwable.stackTraceToString(),
        )
    }

    private fun describeException(throwable: Throwable): String {
        val parts = mutableListOf<String>()
        var current: Throwable? = throwable
        var depth = 0
        while (current != null && depth < 5) {
            val className = current::class.simpleName ?: "Throwable"
            val msg = current.message ?: "<no message>"
            parts += if (depth == 0) "$className: $msg" else "caused by $className: $msg"
            current = current.cause?.takeIf { it !== current }
            depth++
        }
        return parts.joinToString(" | ")
    }

    private fun bytesPreview(
        bytes: ByteArray,
        maxBytes: Int = 64,
    ): String {
        if (bytes.isEmpty()) return "<empty>"
        val preview = bytes.take(maxBytes).toByteArray()

        val hex =
            preview.joinToString(separator = " ") { b ->
                (b.toInt() and 0xFF).toString(16).padStart(2, '0')
            }

        val utf8 =
            runCatching { preview.decodeToString() }
                .getOrNull()
                ?.replace("\n", "\\n")
                ?.replace("\r", "\\r")
                ?.replace("\t", "\\t")

        return buildString {
            append("hex=[")
            append(hex)
            if (bytes.size > maxBytes) append(" …")
            append("]")
            if (!utf8.isNullOrBlank()) {
                append(" utf8=\"")
                append(utf8)
                append("\"")
            }
        }
    }
}
