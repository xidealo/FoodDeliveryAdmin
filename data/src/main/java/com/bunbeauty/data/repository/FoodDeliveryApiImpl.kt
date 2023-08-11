package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.WEB_SOCKET_TAG
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.ServerList
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.enums.OrderStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.SocketException
import javax.inject.Inject

class FoodDeliveryApiImpl @Inject constructor(
    private val client: HttpClient,
    private val json: Json,
) : FoodDeliveryApi {

    private var webSocketSession: DefaultClientWebSocketSession? = null

    private val mutableUpdatedOrderFlow = MutableSharedFlow<ApiResult<OrderServer>>()

    private val mutex = Mutex()

    private var webSocketSessionOpened = false

    override suspend fun login(
        userAuthorizationRequest: UserAuthorizationRequest,
    ): ApiResult<UserAuthorizationResponse> {
        return executePostRequest(
            path = "user/login",
            postBody = userAuthorizationRequest
        )
    }

    override suspend fun getCafeList(cityUuid: String, ): ApiResult<ServerList<CafeServer>> {
        return executeGetRequest(
            path = "cafe",
            parameters = hashMapOf("cityUuid" to cityUuid),
        )
    }

    override suspend fun getCityList(companyUuid: String, ): ApiResult<ServerList<CityServer>> {
        return executeGetRequest(
            path = "city",
            parameters = hashMapOf("companyUuid" to companyUuid),
        )
    }

    override suspend fun getMenuProductList(companyUuid: String): ServerList<MenuProductServer> {
        return withContext(IO) {
            val request = client.get {
                url {
                    path("menu_product")
                }
                parameter("companyUuid", companyUuid)
            }

            json.decodeFromString(
                ServerList.serializer(MenuProductServer.serializer()),
                request.bodyAsText()
            )
        }
    }

    override suspend fun deleteMenuProductPhoto(photoName: String) {

    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String> {
        return ApiResult.Success(":")
    }

    override suspend fun updateVisibleMenuProductUseCase(
        uuid: String,
        isVisible: Boolean,
        token: String,
    ) {
        client.patch {
            url {
                path("menu_product")
            }
            parameter("uuid", uuid)
            setBody(
                MenuProductServer(
                    isVisible = isVisible
                )
            )
            header("Authorization", "Bearer $token")
        }
    }

    override suspend fun deleteMenuProduct(uuid: String) {

    }

    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<StatisticServer> {
        return client.get {
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

    private fun subscribeOnOrderUpdates(token: String, cafeUuid: String) {
        CoroutineScope(Job() + IO).launch {
            try {
                webSocketSessionOpened = true
                client.webSocket(
                    HttpMethod.Get,
                    path = "user/order/subscribe",
                    port = 80,
                    request = {
                        header("Authorization", "Bearer $token")
                        parameter("cafeUuid", cafeUuid)
                    }
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
                val stackTrace = exception.stackTrace.joinToString("\n") {
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
    ): ApiResult<ServerList<OrderServer>> {
        return executeGetRequest(
            path = "order",
            parameters = hashMapOf("cafeUuid" to cafeUuid),
            token = token
        )
    }

    override suspend fun getOrderByUuid(
        token: String,
        orderUuid: String,
    ): ApiResult<OrderDetailsServer> {
        return executeGetRequest(
            path = "v2/order/details",
            parameters = hashMapOf("uuid" to orderUuid),
            token = token
        )
    }

    override suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus,
    ): ApiResult<OrderDetailsServer> {
        return patchData(
            path = "order",
            patchBody = hashMapOf("status" to status.toString()),
            serializer = OrderDetailsServer.serializer(),
            parameters = hashMapOf("uuid" to orderUuid),
            token = token
        )
    }

    override suspend fun getCategoriesByCompanyUuid(
        token: String,
        companyUuid: String,
    ): ApiResult<ServerList<CategoryServer>> {
        return executeGetRequest(
            path = "category",
            parameters = hashMapOf("companyUuid" to companyUuid),
            token = token
        )
    }

    private suspend inline fun <reified T> executeGetRequest(
        path: String,
        parameters: Map<String, String?> = mapOf(),
        token: String? = null,
    ): ApiResult<T> {
        return safeCall {
            client.get {
                buildRequest(
                    path = path,
                    body = null,
                    parameters = parameters,
                    headers = if (token == null) emptyMap() else mapOf("Authorization" to "Bearer $token")
                )
            }.body()
        }
    }

    private suspend inline fun <reified T> executePostRequest(
        path: String,
        postBody: Any,
        parameters: Map<String, String> = mapOf(),
        token: String? = null,
    ): ApiResult<T> {
        return safeCall {
            client.post {
                buildRequest(
                    path = path,
                    body = postBody,
                    parameters = parameters,
                    headers = token?.let {
                        mapOf("Authorization" to "Bearer $token")
                    } ?: emptyMap()
                )
            }.body()
        }
    }

    private suspend fun <T> patchData(
        path: String,
        patchBody: Any,
        serializer: KSerializer<T>,
        parameters: Map<String, String> = mapOf(),
        token: String? = null,
    ): ApiResult<T> {
        val request = client.patch {
            buildRequest(
                path = path,
                body = patchBody,
                parameters = parameters,
                headers = token?.let {
                    mapOf("Authorization" to "Bearer $token")
                } ?: emptyMap()
            )
        }
        return handleResponse(serializer, request)
    }

    private fun HttpRequestBuilder.buildRequest(
        path: String,
        body: Any?,
        parameters: Map<String, String?> = mapOf(),
        headers: Map<String, String> = mapOf(),
    ) {
        if (body != null) {
            setBody(body)
        }
        url {
            path(path)
        }
        parameters.forEach { parameterMap ->
            parameter(parameterMap.key, parameterMap.value)
        }
        headers.forEach { headerEntry ->
            header(headerEntry.key, headerEntry.value)
        }
    }

    private suspend fun <T> handleResponse(
        serializer: KSerializer<T>,
        request: HttpResponse,
    ): ApiResult<T> {
        return try {
            ApiResult.Success(json.decodeFromString(serializer, request.bodyAsText()))
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
        } catch (exception: Exception) {
            ApiResult.Error(ApiError(0, exception.message ?: "-"))
        }
    }

    private suspend inline fun <reified R> safeCall(
        networkCall: () -> HttpResponse,
    ): ApiResult<R> {
        return try {
            ApiResult.Success(networkCall().body())
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
        } catch (exception: Throwable) {
            ApiResult.Error(ApiError(0, exception.message ?: "Bad Internet"))
        }
    }

}