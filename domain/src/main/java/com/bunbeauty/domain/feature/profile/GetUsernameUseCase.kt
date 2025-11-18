package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetUsernameUseCase(
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(): String = dataStoreRepo.username.firstOrNull() ?: throw DataNotFoundException()
}
