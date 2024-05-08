package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.LoginException
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userAuthorizationRepo: UserAuthorizationRepo
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Triple<String, String, String> {
        return userAuthorizationRepo.login(username = username, password = password)
            ?: throw LoginException()
    }
}
