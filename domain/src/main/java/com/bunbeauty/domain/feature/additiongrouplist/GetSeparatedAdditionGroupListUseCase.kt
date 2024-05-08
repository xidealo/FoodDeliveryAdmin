package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

data class SeparatedAdditionGroupList(
    val visibleList: List<AdditionGroup>,
    val hiddenList: List<AdditionGroup>
)

class GetSeparatedAdditionGroupListUseCase @Inject constructor(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): SeparatedAdditionGroupList {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val additionGroupList = additionGroupRepo.getAdditionGroupList(token = token)

        return SeparatedAdditionGroupList(
            visibleList = additionGroupList
                .filter { additionGroup ->
                    additionGroup.isVisible
                }
                .sortedBy { addition ->
                    addition.name
                },
            hiddenList = additionGroupList
                .filterNot { additionGroup ->
                    additionGroup.isVisible
                }
                .sortedBy { additionGroup ->
                    additionGroup.name
                }
        )
    }
}
