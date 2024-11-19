package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.SettingsRepo
import javax.inject.Inject

class GetIsUnlimitedNotificationUseCase @Inject constructor(
    private val settingsRepo: SettingsRepo
) {

    suspend operator fun invoke(): Boolean {
        return settingsRepo.isUnlimitedNotification()
    }
}
