package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: NetworkConnector
) : UserAuthorizationRepo {

    override suspend fun login(
        username: String,
        password: String
    ): ApiResult<Triple<String, String, String>> {
        return withContext(IO) {
            when (val result = networkConnector.login(
                UserAuthorizationRequest(
                    username = username,
                    password = password
                )
            )) {
                is ApiResult.Success -> {
                    ApiResult.Success(
                        Triple(
                            result.data.token,
                            result.data.cityUuid,
                            result.data.companyUuid
                        )
                    )
                }

                is ApiResult.Error -> {
                    ApiResult.Error(result.apiError)
                }
            }
        }
    }

}