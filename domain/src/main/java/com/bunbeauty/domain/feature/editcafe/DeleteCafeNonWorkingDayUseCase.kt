package com.bunbeauty.domain.feature.editcafe

import com.bunbeauty.domain.exception.DataDeletingFailedException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo

class DeleteCafeNonWorkingDayUseCase (
    private val nonWorkingDayRepo: NonWorkingDayRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(uuid: String) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        nonWorkingDayRepo.updateNonWorkingDay(
            token = token,
            uuid = uuid,
            isVisible = false
        ) ?: throw DataDeletingFailedException()
    }
}
