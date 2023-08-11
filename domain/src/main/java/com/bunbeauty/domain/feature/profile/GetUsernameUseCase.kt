package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
) {

    suspend operator fun invoke(): String {
        return dataStoreRepo.username.firstOrNull() ?: throw DataNotFoundException()
    }
}