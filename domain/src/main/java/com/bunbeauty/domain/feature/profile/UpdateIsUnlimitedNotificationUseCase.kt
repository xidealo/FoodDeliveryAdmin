package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.SettingsRepo
import javax.inject.Inject

class UpdateIsUnlimitedNotificationUseCase @Inject constructor(
    private val settingsRepo: SettingsRepo
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        settingsRepo.updateUnlimitedNotification(isEnabled = isEnabled)
    }
}
