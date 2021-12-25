package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.model.server.response.UserAuthorizationResponse
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: NetworkConnector
) : UserAuthorizationRepo {

    override suspend fun login(
        username: String,
        password: String
    ): ApiResult<Pair<String, String>> {
        return when (val result = networkConnector.login(
            UserAuthorizationRequest(
                username = username,
                password = password
            )
        )) {
            is ApiResult.Success -> {
                ApiResult.Success(Pair(result.data.token, result.data.managerCity))
            }

            is ApiResult.Error -> {
                ApiResult.Error(result.apiError)
            }
        }
    }

}