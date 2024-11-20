package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additionlist.exception.NotFoundAdditionException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class GetAdditionUseCase @Inject constructor(
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
