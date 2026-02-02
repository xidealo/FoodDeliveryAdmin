package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import common.ApiResult

abstract class BaseUserAuthorizationRepository(
    protected val networkConnector: FoodDeliveryApi,
    protected val dataStoreRepo: DataStoreRepo,
) : UserAuthorizationRepo {
    override suspend fun login(
        username: String,
        password: String,
    ): LoginUser? =
        when (
            val result =
                networkConnector.login(
                    UserAuthorizationRequest(
                        username = username,
                        password = password,
                    ),
                )
        ) {
            is ApiResult.Success ->
                LoginUser(
                    token = result.data.token,
                    cafeUuid = result.data.cafeUuid,
                    companyUuid = result.data.companyUuid,
                )

            is ApiResult.Error -> null
        }

    override suspend fun clearNotificationToken() {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        networkConnector.deleteNotificationToken(token = token)
    }
}
