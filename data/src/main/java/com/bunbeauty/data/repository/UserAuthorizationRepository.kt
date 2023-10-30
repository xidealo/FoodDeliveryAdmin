package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.exception.LoginException
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi
) : UserAuthorizationRepo {

    override suspend fun login(
        username: String,
        password: String
    ): Triple<String, String, String> {
        return when (val result = networkConnector.login(
            UserAuthorizationRequest(
                username = username,
                password = password
            )
        )) {
            is ApiResult.Success -> {
                Triple(
                    result.data.token,
                    result.data.cityUuid,
                    result.data.companyUuid
                )
            }

            is ApiResult.Error -> throw LoginException()
        }
    }

}