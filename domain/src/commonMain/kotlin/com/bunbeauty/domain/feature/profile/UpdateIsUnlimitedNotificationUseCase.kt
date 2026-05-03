package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.SettingsRepo

class UpdateIsUnlimitedNotificationUseCase(
    private val settingsRepo: SettingsRepo,
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        settingsRepo.updateUnlimitedNotification(isEnabled = isEnabled)
    }
}
