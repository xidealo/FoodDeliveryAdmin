package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.updateaddition.NotFoundAdditionException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import javax.inject.Inject

class GetAdditionUseCase @Inject constructor(
    private val additionRepo: AdditionRepo
) {
    suspend operator fun invoke(additionUuid: String): Addition {
        return additionRepo.getAddition(additionUuid = additionUuid)
            ?: throw NotFoundAdditionException()
    }
}