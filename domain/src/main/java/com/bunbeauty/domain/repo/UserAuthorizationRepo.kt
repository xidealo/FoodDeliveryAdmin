package com.bunbeauty.domain.repo

import com.bunbeauty.common.ApiResult

interface UserAuthorizationRepo {
    suspend fun login(username: String, password: String): ApiResult<Triple<String, String, String>>
}