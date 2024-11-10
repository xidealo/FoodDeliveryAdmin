package com.bunbeauty.domain.feature.login

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.SettingsRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import javax.inject.Inject

class CheckAuthorizationUseCase @Inject constructor(
    private val userAuthorizationRepo: UserAuthorizationRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val settingsRepo: SettingsRepo,
) {

    suspend operator fun invoke(): Boolean {
        val token = dataStoreRepo.getToken()
        if (token != null) {
            userAuthorizationRepo.updateNotificationToken()
            settingsRepo.init()
        }

        return token != null
    }
}
