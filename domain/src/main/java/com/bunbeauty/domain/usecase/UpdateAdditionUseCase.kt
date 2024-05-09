package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
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
        isVisible: Boolean
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        if (updateAddition.name.isNullOrBlank()) {
            throw AdditionNameException()
        }
        if (updateAddition.priority == null || updateAddition.price == 0) {
            throw AdditionPriorityException()
        }

        additionRepo.updateAddition(
            additionUuid = additionUuid,
            updateAddition = UpdateAddition(isVisible = isVisible),
            token = token
        )
    }
}
