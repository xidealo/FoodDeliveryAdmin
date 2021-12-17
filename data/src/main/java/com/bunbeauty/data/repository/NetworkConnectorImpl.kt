package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.data.model.server.cafe.ServerCafe
import com.bunbeauty.data.model.server.ServerMenuProduct
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.ListServer
import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.data.model.server.UserAuthorization
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class NetworkConnectorImpl @Inject constructor(
    private val dateTimeUtil: IDateTimeUtil,
    private val client: HttpClient,
    private val json: Json
) : NetworkConnector {

    override suspend fun login(userAuthorization: UserAuthorization): ApiResult<UserAuthorization> {
        return postData(
            path = "user/login",
            postBody = userAuthorization,
            serializer = UserAuthorization.serializer()
        )
    }

    override suspend fun subscribeOnNotification() {

    }

    override suspend fun unsubscribeOnNotification() {

    }

    override suspend fun getCafeList(): ApiResult<ListServer<ServerCafe>> {
        return ApiResult.Success(ListServer(1, listOf(ServerCafe())))
        //return getData("")
    }

    override suspend fun getDelivery(): ApiResult<Delivery> {
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

    override suspend fun getOrderListByCafeId(cafeId: String): Flow<ApiResult<ListServer<ServerOrder>>> {
        return flow {
            emit(ApiResult.Success(ListServer(1, listOf(ServerOrder()))))
            //emit(ApiResult.Error(ApiError(1, "ss")))
        }
        //return getData("")
    }

    override suspend fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus) {

    }

    suspend fun <T : Any> getData(
        path: String,
        serializer: KSerializer<T>,
        parameters: HashMap<String, String> = hashMapOf()
    ): ApiResult<T> {
        return try {
            ApiResult.Success(
                json.decodeFromString(
                    serializer,
                    client.get<HttpStatement> {
                        url {
                            path(path)
                        }
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
        parameters: HashMap<String, String> = hashMapOf()
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
        parameters: HashMap<String, String> = hashMapOf()
    ): ApiResult<R> {
        return try {
            ApiResult.Success(
                json.decodeFromString(
                    serializer,
                    client.patch<HttpStatement>(body = body) {
                        url {
                            path(path)
                        }
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

}