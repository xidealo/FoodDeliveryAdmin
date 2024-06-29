package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class UpdateAdditionUseCase @Inject constructor(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        additionUuid: String,
        updateAddition: UpdateAddition,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            updateAddition.name.isNullOrBlank() -> throw AdditionNameException()
            updateAddition.priority == null || updateAddition.prise == 0 -> throw AdditionPriorityException()

        }

        additionRepo.updateAddition(additionUuid = additionUuid, token = token, updateAddition = updateAddition)
    }
}
