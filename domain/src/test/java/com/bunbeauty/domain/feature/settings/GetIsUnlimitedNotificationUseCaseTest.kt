package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.repo.SettingsRepo
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetIsUnlimitedNotificationUseCaseTest {
    private val settingsRepo: SettingsRepo = mockk()

    private val getIsUnlimitedNotificationUseCase = GetIsUnlimitedNotificationUseCase(settingsRepo)

    @Test
    fun `invoke should return true when isUnlimitedNotification returns true`() =
        runTest {
            // Given
            coEvery { settingsRepo.isUnlimitedNotification() } returns true

            // When
            val result = getIsUnlimitedNotificationUseCase()

            // Then
            assertEquals(true, result)
        }

    @Test
    fun `invoke should return false when isUnlimitedNotification returns false`() =
        runTest {
            // Given
            coEvery { settingsRepo.isUnlimitedNotification() } returns false

            // When
            val result = getIsUnlimitedNotificationUseCase()

            // Then
            assertEquals(false, result)
        }
}
