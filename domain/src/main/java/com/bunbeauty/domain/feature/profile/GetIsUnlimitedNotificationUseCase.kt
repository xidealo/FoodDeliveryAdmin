package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.SettingsRepo

class GetIsUnlimitedNotificationUseCase (
    private val settingsRepo: SettingsRepo
) {

    suspend operator fun invoke(): Boolean {
        return settingsRepo.isUnlimitedNotification()
    }
}
