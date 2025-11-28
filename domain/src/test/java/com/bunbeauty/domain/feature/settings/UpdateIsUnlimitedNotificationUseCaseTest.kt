package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.repo.SettingsRepo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UpdateIsUnlimitedNotificationUseCaseTest {
    private val settingsRepo: SettingsRepo = mockk(relaxed = true)

    private val updateIsUnlimitedNotificationUseCase = UpdateIsUnlimitedNotificationUseCase(settingsRepo)

    @Test
    fun `invoke should call updateUnlimitedNotification with true`() = runTest {
        updateIsUnlimitedNotificationUseCase.invoke(isEnabled = true)

        coVerify { settingsRepo.updateUnlimitedNotification(isEnabled = true) }
    }

    @Test
    fun `invoke should call updateUnlimitedNotification with false`() = runTest {
        updateIsUnlimitedNotificationUseCase.invoke(isEnabled = false)

        coVerify { settingsRepo.updateUnlimitedNotification(isEnabled = false) }
    }
}
