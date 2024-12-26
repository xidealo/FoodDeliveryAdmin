package com.bunbeauty.domain.feature.login

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo

class CheckAuthorizationUseCase(
    private val userAuthorizationRepo: UserAuthorizationRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(): Boolean {
        val token = dataStoreRepo.getToken()
        if (token != null) {
            userAuthorizationRepo.updateNotificationToken()
        }

        return token != null
    }
}
