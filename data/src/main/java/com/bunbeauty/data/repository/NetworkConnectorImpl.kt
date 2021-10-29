package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.COMPANY_UUID_PARAMETER
import com.bunbeauty.common.Constants.WEB_SOCKET_TAG
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.CategoryServer
import com.bunbeauty.data.model.server.DeliveryServer
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class NetworkConnectorImpl @Inject constructor(
    private val dateTimeUtil: IDateTimeUtil,
    private val client: HttpClient,
    private val json: Json
) : NetworkConnector {

    private var webSocketSession: DefaultClientWebSocketSession? = null

    override suspend fun login(userAuthorizationRequest: UserAuthorizationRequest): ApiResult<UserAuthorizationResponse> {
        return postData(
            path = "user/login",
            postBody = userAuthorizationRequest,
            serializer = UserAuthorizationResponse.serializer(),
            token = ""
        )
    }

    override suspend fun subscribeOnNotification() {

    }

    override suspend fun unsubscribeOnNotification(cafeId: String) {

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
        //return getData("")
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

/*
    override suspend fun saveMenuProduct(menuProduct: ServerMenuProduct) {

    }

    override suspend fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String) {

    }
*/

    override suspend fun deleteMenuProduct(uuid: String) {

    }

    override suspend fun getStatistic(
        token: String,
        cafeUuid: String,
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

    override suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeId: String
    ): Flow<ApiResult<ServerOrder>> {

        Firebase.messaging.subscribeToTopic(cafeId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NotificationTag", "Subscribe to topic is successful")
                } else {
                    Log.d("NotificationTag", "Subscribe to topic is not successful")
                }
            }

        return flow {
            try {
                Log.d(WEB_SOCKET_TAG, "in socket")
                client.webSocket(
                    HttpMethod.Get,
                    path = "user/order/subscribe",
                    request = {
                        header("Authorization", "Bearer $token")
                        parameter("cafeUuid", cafeId)
                    }
                ) {
                    webSocketSession = this
                    while (true) {
                        val otherMessage = incoming.receive() as? Frame.Text ?: continue
                        Log.d(WEB_SOCKET_TAG, otherMessage.readText())
                        emit(
                            ApiResult.Success(
                                json.decodeFromString(
                                    ServerOrder.serializer(),
                                    otherMessage.readText()
                                )
                            ),
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d(WEB_SOCKET_TAG, e.message ?: "")
            }
        }
    }

    override suspend fun unsubscribeOnOrderList(cafeId: String) {
        Firebase.messaging.unsubscribeFromTopic(cafeId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NotificationTag", "Unsubscribe to topic is successful")
                } else {
                    Log.d("NotificationTag", "Unsubscribe to topic is not successful")
                }
            }

        if (webSocketSession != null) {
            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, "Change cafe"))
            webSocketSession = null
            Log.d(WEB_SOCKET_TAG, "webSocketSession closed")
        }
    }

    override suspend fun getOrderListByCafeId(
        token: String,
        cafeId: String
    ): ApiResult<ListServer<ServerOrder>> {
        return getData(
            path = "order",
            serializer = ListServer.serializer(ServerOrder.serializer()),
            parameters = hashMapOf("cafeUuid" to cafeId),
            token = token
        )
    }

    override suspend fun updateOrderStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus
    ): ApiResult<ServerOrder> {
        return patchData(
            path = "order",
            body = hashMapOf("status" to status.toString()),
            serializer = ServerOrder.serializer(),
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


    suspend fun <T : Any> getData(
        path: String,
        serializer: KSerializer<T>,
        parameters: HashMap<String, String> = hashMapOf(),
        token: String
    ): ApiResult<T> {
        return try {
            ApiResult.Success(
                json.decodeFromString(
                    serializer,
                    client.get<HttpStatement> {
                        url {
                            path(path)
                        }
                        if (token.isNotEmpty())
                            header("Authorization", "Bearer $token")

                        parameters.forEach { parameterMap ->
                            parameter(parameterMap.key, parameterMap.value)
                        }
                    }.execute().readText()
                )
            )
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message ?: "-"))
        } catch (exception: Exception) {
            ApiResult.Error(ApiError(0, exception.message ?: "-"))
        }
    }

    suspend fun <T : Any, R> postData(
        path: String,
        postBody: T,
        serializer: KSerializer<R>,
        parameters: HashMap<String, String> = hashMapOf(),
        token: String
    ): ApiResult<R> {
        return try {
            ApiResult.Success(
                json.decodeFromString(
                    serializer,
                    client.post<HttpStatement>(body = postBody) {
                        contentType(ContentType.Application.Json)
                        url {
                            path(path)
                        }
                        header("Authorization", "Bearer $token")
                        parameters.forEach { parameterMap ->
                            parameter(parameterMap.key, parameterMap.value)
                        }
                    }.execute().readText()
                )
            )
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
        } catch (exception: Exception) {
            ApiResult.Error(ApiError(0, exception.message ?: "-"))
        }
    }

    suspend fun <T : Any, R> patchData(
        path: String,
        body: T,
        serializer: KSerializer<R>,
        parameters: HashMap<String, String> = hashMapOf(),
        token: String
    ): ApiResult<R> {
        return try {
            ApiResult.Success(
                json.decodeFromString(
                    serializer,
                    client.patch<HttpStatement>(body = body) {
                        url {
                            path(path)
                        }
                        header("Authorization", "Bearer $token")
                        parameters.forEach { parameterMap ->
                            parameter(parameterMap.key, parameterMap.value)
                        }
                    }.execute().readText()
                )
            )
        } catch (exception: ClientRequestException) {
            ApiResult.Error(ApiError(exception.response.status.value, exception.message))
        } catch (exception: Exception) {
            ApiResult.Error(ApiError(0, exception.message ?: "-"))
        }
    }

}