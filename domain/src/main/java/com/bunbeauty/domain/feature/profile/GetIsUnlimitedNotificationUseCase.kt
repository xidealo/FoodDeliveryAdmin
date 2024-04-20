package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetIsUnlimitedNotificationUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(): Boolean {
        return dataStoreRepo.isUnlimitedNotification.firstOrNull() ?: throw DataNotFoundException()
    }
}
