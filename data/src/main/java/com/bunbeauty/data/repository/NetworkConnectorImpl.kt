package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.ServerMenuProduct
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
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

    override suspend fun unsubscribeOnNotification() {

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

    override suspend fun getDelivery(token: String, cityUuid: String): ApiResult<Delivery> {
        return ApiResult.Success(Delivery())
        //return getData("")

    }

    override suspend fun getMenuProductList(): ApiResult<ListServer<ServerMenuProduct>> {
        return ApiResult.Success(ListServer(1, listOf(ServerMenuProduct())))
        //return getData("")
    }

    override suspend fun deleteMenuProductPhoto(photoName: String) {

    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): ApiResult<String> {
        return ApiResult.Success(":")
    }

    override suspend fun saveMenuProduct(menuProduct: ServerMenuProduct) {

    }

    override suspend fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String) {

    }

    override suspend fun deleteMenuProduct(uuid: String) {

    }

    override suspend fun getStatistic(period: String): ApiResult<ListServer<StatisticServer>> {
        return ApiResult.Success(ListServer(3, listOf(StatisticServer())))
    }

    override suspend fun subscribeOnOrderListByCafeId(
        token: String,
        cafeId: String
    ): Flow<ApiResult<ServerOrder>> {
        return flow {
            try {
                Log.d("try", "in socket")
                client.webSocket(
                    HttpMethod.Get,
                    path = "user/order/subscribe",
                    request = {
                        header("Authorization", "Bearer $token")
                        parameter("cafeUuid", cafeId)
                    }
                ) {
                    Log.d("aaa", "in socket")
                    while (true) {
                        val otherMessage = incoming.receive() as? Frame.Text ?: continue
                        Log.d("aaa", otherMessage.readText())
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
                Log.d("aaa", e.message ?: "")
            }
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
    ) :ApiResult<ServerOrder> {
        return patchData(
            path = "order",
            body = hashMapOf("status" to status.toString()),
            serializer = ServerOrder.serializer(),
            parameters = hashMapOf("uuid" to orderUuid),
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