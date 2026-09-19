package com.bunbeauty.domain.feature.login

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.domain.usecase.LogoutUseCase

class CheckAuthorizationUseCase(
    private val userAuthorizationRepo: UserAuthorizationRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val logoutUseCase: LogoutUseCase,
) {
    suspend operator fun invoke(): Boolean {
        val token = dataStoreRepo.getToken()
        if (token == null) {
            return false
        }

        return when (userAuthorizationRepo.validateSession()) {
            SessionValidationResult.VALID -> {
                userAuthorizationRepo.updateNotificationToken()
                true
            }

            SessionValidationResult.INVALID -> {
                logoutUseCase()
                false
            }

            SessionValidationResult.UNAVAILABLE -> true
        }
    }
}
