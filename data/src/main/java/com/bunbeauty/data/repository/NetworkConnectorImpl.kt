package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.COMPANY_UUID_PARAMETER
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.common.Constants.WEB_SOCKET_TAG
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.DeliveryServer
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.model.server.order.ServerOrderDetails
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.enums.OrderStatus
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkConnectorImpl @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) : NetworkConnector {

    private var webSocketSession: DefaultClientWebSocketSession? = null

    override suspend fun login(userAuthorizationRequest: UserAuthorizationRequest): ApiResult<UserAuthorizationResponse> {
        return postData(
            path = "user/login",
            postBody = userAuthorizationRequest,
            serializer = UserAuthorizationResponse.serializer()
        )
    }

    override suspend fun getCafeList(
        token: String,
        cityUuid: String
    ): ApiResult<ListServer<CafeServer>> {
        return getData(
            path = "cafe",
            serializer = ListServer.serializer(CafeServer.serializer()),
            parameters = hashMapOf("cityUuid" to cityUuid),
            token = token,
        )
    }

    override suspend fun getDelivery(
        token: String,
        companyUuid: String
    ): ApiResult<DeliveryServer> {
        return getData(
            token = token,
            path = "delivery",
            serializer = DeliveryServer.serializer(),
            parameters = hashMapOf(COMPANY_UUID_PARAMETER to companyUuid)
        )
    }

    override suspend fun getMenuProductList(companyUuid: String): ApiResult<ListServer<MenuProductServer>> {
        return getData(
            path = "menu_product",
            serializer = ListServer.serializer(MenuProductServer.serializer()),
            parameters = hashMapOf("companyUuid" to companyUuid),
            token = ""
        )
    }

    override suspend fun deleteMenuProductPhoto(photoName: String) {

    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String> {
        return ApiResult.Success(":")
    }

    override suspend fun deleteMenuProduct(uuid: String) {

    }

    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): ApiResult<ListServer<StatisticServer>> {
        return getData(
            path = "statistic",
            serializer = ListServer.serializer(StatisticServer.serializer()),
            parameters = hashMapOf(
                "cafeUuid" to cafeUuid,
                "period" to period,
            ),
            token = token,
        )
    }

    override suspend fun subscribeOnCafeTopic(cafeUuid: String) {
        Firebase.messaging.subscribeToTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Subscription to $cafeUuid is successful")
                } else {
                    Log.d(NOTIFICATION_TAG, "Subscription to $cafeUuid failed")
                }
            }
    }

    override suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeUuid: String
    ): Flow<ApiResult<ServerOrder>> {
        return flow {
            try {
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
                            json.decodeFromString(ServerOrder.serializer(), message.readText())
                        emit(ApiResult.Success(serverModel))
                    }
                }
            } catch (e: WebSocketException) {
                Log.e(WEB_SOCKET_TAG, "WebSocketException: ${e.message}")
            } catch (e: Exception) {
                val stackTrace = e.stackTrace.joinToString("\n") {
                    "${it.className} ${it.methodName} ${it.lineNumber}"
                }
                Log.e(
                    WEB_SOCKET_TAG, "Exception: $e \n$stackTrace"
                )
            } finally {
                unsubscribeOnOrderList("Unknown")
            }
        }
    }

    override suspend fun unsubscribeFromCafeTopic(cafeUuid: String) {
        Firebase.messaging.unsubscribeFromTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Unsubscription from $cafeUuid is successful")
                } else {
                    Log.d(NOTIFICATION_TAG, "Unsubscription from $cafeUuid failed")
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

    override suspend fun getOrderListByCafeId(
        token: String,
        cafeUuid: String
    ): ApiResult<ListServer<ServerOrder>> {
        return getData(
            path = "order",
            serializer = ListServer.serializer(ServerOrder.serializer()),
            parameters = hashMapOf("cafeUuid" to cafeUuid),
            token = token
        )
    }

    override suspend fun getOrderByUuid(
        token: String,
        orderUuid: String
    ): ApiResult<ServerOrderDetails> {
        return getData(
            path = "order/details",
            serializer = ServerOrderDetails.serializer(),
            parameters = hashMapOf("uuid" to orderUuid),
            token = token
        )
    }

    override suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus
    ): ApiResult<ServerOrderDetails> {
        return patchData(
            path = "order",
            patchBody = hashMapOf("status" to status.toString()),
            serializer = ServerOrderDetails.serializer(),
            parameters = hashMapOf("uuid" to orderUuid),
            token = token
        )
    }

    override suspend fun getCategoriesByCompanyUuid(
        token: String,
        companyUuid: String
    ): ApiResult<ListServer<CategoryServer>> {
        return getData(
            path = "category",
            serializer = ListServer.serializer(CategoryServer.serializer()),
            parameters = hashMapOf("companyUuid" to companyUuid),
            token = token
        )
    }


//    suspend fun <T : Any> getData(
//        path: String,
//        serializer: KSerializer<T>,
//        parameters: HashMap<String, String?> = hashMapOf(),
//        token: String
//    ): ApiResult<T> {
//        return try {
//            ApiResult.Success(
//                json.decodeFromString(
//                    serializer,
//                    client.get<HttpStatement> {
//                        url {
//                            path(path)
//                        }
//                        if (token.isNotEmpty())
//                            header("Authorization", "Bearer $token")
//
//                        parameters.forEach { (key, parameter) ->
//                            if (parameter != null) {
//                                parameter(key, parameter)
//                            }
//                        }
//                    }.execute().readText()
//                )
//            )
//        } catch (exception: ClientRequestException) {
//            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
//        } catch (exception: CancellationException) {
//            ApiResult.Error(ApiError(7, exception.message ?: ""))
//        } catch (exception: Exception) {
//            ApiResult.Error(ApiError(0, exception.message ?: ""))
//        }
//    }

    suspend fun <T> getData(
        path: String,
        serializer: KSerializer<T>,
        parameters: Map<String, String?> = mapOf(),
        token: String
    ): ApiResult<T> {
        val request = client.get {
            buildRequest(
                path = path,
                body = null,
                parameters = parameters,
                headers = mapOf("Authorization" to "Bearer $token")
            )
        }
        return handleResponse(serializer, request)
    }

    suspend fun <T> postData(
        path: String,
        postBody: Any,
        serializer: KSerializer<T>,
        parameters: Map<String, String> = mapOf(),
        token: String? = null
    ): ApiResult<T> {
        val request = client.post {
            buildRequest(
                path = path,
                body = postBody,
                parameters = parameters,
                headers = token?.let {
                    mapOf("Authorization" to "Bearer $token")
                } ?: emptyMap()
            )
        }
        return handleResponse(serializer, request)
    }

    suspend fun <T> patchData(
        path: String,
        patchBody: Any,
        serializer: KSerializer<T>,
        parameters: Map<String, String> = mapOf(),
        token: String? = null
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

    fun HttpRequestBuilder.buildRequest(
        path: String,
        body: Any?,
        parameters: Map<String, String?> = mapOf(),
        headers: Map<String, String> = mapOf()
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

    suspend fun <T> handleResponse(
        serializer: KSerializer<T>,
        request: HttpResponse
    ): ApiResult<T> {
        return try {
            ApiResult.Success(json.decodeFromString(serializer, request.bodyAsText()))
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
        } catch (exception: Exception) {
            ApiResult.Error(ApiError(0, exception.message ?: "-"))
        }
    }

}