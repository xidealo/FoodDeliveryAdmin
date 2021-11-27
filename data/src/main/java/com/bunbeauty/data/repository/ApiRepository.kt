package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.cafe.server.ServerCafe
import com.bunbeauty.domain.model.menu_product.ServerMenuProduct
import com.bunbeauty.domain.model.order.server.ServerOrder
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val dateTimeUtil: IDateTimeUtil,
    private val client: HttpClient,
    private val json: Json
) : ApiRepo {

    private val serverOrderList = LinkedList<ServerOrder>()

    override fun login(login: String, passwordHash: String): Flow<Boolean> {
        return flow {

        }
    }

    override fun subscribeOnNotification() {

    }

    override fun unsubscribeOnNotification() {

    }

    override val cafeList: Flow<List<ServerCafe>>
        get() = flow {

        }
    override val delivery: Flow<Delivery>
        get() = flow {

        }
    override val menuProductList: Flow<List<ServerMenuProduct>>
        get() = flow {

        }

    override fun deleteMenuProductPhoto(photoName: String) {

    }

    override fun saveMenuProductPhoto(photoByteArray: ByteArray): Flow<String> {
        return flow { }
    }

    override fun saveMenuProduct(menuProduct: ServerMenuProduct) {

    }

    override fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String) {

    }

    override fun deleteMenuProduct(uuid: String) {

    }

    override fun getAddedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>> {
        return flow { }
    }

    override fun getUpdatedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>> {
        return flow { }
    }

    override val orderList: Flow<List<ServerOrder>>
        get() = flow {

        }

    override fun getOrderListByCafeId(cafeId: String): Flow<List<ServerOrder>> {
        return flow { }
    }

    override fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus) {

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
            ApiResult.Error(ApiError(exception.response.status.value, exception.message ?: "-"))
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