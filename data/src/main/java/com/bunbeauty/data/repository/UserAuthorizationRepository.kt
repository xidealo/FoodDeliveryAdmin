package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.UserAuthorization
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: NetworkConnector
) : UserAuthorizationRepo {

    override suspend fun login(username: String, password: String): ApiResult<String> {
        return when (val result = networkConnector.login(
            UserAuthorization(
                username = username,
                password = password
            )
        )) {
            is ApiResult.Success -> {
                ApiResult.Success(data = result.data?.token ?: "")
            }

            is ApiResult.Error -> {
                ApiResult.Error(result.apiError)
            }
        }
    }

}