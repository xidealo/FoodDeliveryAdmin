package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

data class SeparatedAdditionList(
    val visibleList: List<Addition>,
    val hiddenList: List<Addition>
)

class GetSeparatedAdditionListUseCase @Inject constructor(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): SeparatedAdditionList {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val additionList = additionRepo.getAdditionList(token = token)

        return SeparatedAdditionList(
            visibleList = additionList
                .filter { addition ->
                    addition.isVisible
                }
                .sortedBy { addition ->
                    addition.name
                },
            hiddenList = additionList
                .filterNot { addition ->
                    addition.isVisible
                }
                .sortedBy { addition ->
                    addition.name
                },
        )
    }
}