package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class UpdateVisibleAdditionUseCase @Inject constructor(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
    ) {
        suspend operator fun invoke( additionUuid: String, isVisible: Boolean) {
            val token = dataStoreRepo.getToken() ?: throw NoTokenException()

            additionRepo.updateAddition(
                additionUuid = additionUuid,
                updateAddition = UpdateAddition(isVisible = isVisible),
                token = token
            )

        }
    }
