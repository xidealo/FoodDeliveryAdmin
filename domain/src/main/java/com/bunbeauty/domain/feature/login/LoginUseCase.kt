package com.bunbeauty.domain.feature.login

import com.bunbeauty.domain.exception.LoginException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo


class LoginUseCase (
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
