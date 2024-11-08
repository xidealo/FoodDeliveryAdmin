package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.LoginException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userAuthorizationRepo: UserAuthorizationRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ) {
        val (token, cityUuid, companyUuid) = userAuthorizationRepo.login(
            username = username,
            password = password
        ) ?: throw LoginException()
        dataStoreRepo.apply {
            saveToken(token)
            saveManagerCity(cityUuid)
            saveCompanyUuid(companyUuid)
            saveUsername(username)
        }
        userAuthorizationRepo.updateNotificationToken()
    }
}
