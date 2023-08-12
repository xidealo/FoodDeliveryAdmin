package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class UpdateIsUnlimitedNotificationUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
) {

    suspend operator fun invoke(isUnlimitedNotification: Boolean) {
        dataStoreRepo.saveIsUnlimitedNotification(isUnlimitedNotification)
    }
}