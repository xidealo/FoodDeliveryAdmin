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
        return networkConnector.login(
            UserAuthorization(
                username = username,
                password = password
            )
        )
    }

}