package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.SettingsRepo
import javax.inject.Inject

class GetIsUnlimitedNotificationUseCase @Inject constructor(
    private val settingsRepo: SettingsRepo
) {

    operator fun invoke(): Boolean {
        return settingsRepo.isUnlimitedNotification()
    }
}
