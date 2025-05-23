package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.NotFoundAdditionException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class GetAdditionUseCase(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(additionUuid: String): Addition {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        return additionRepo.getAddition(
            additionUuid = additionUuid,
            token = token
        ) ?: throw NotFoundAdditionException()
    }
}
