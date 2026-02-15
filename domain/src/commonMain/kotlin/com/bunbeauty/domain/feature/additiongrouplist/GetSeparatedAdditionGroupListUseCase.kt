package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo

data class SeparatedAdditionGroupList(
    val visibleList: List<AdditionGroup>,
    val hiddenList: List<AdditionGroup>,
) {
    companion object {
        val mock =
            SeparatedAdditionGroupList(
                visibleList = emptyList(),
                hiddenList = emptyList(),
            )
    }
}

class GetSeparatedAdditionGroupListUseCase(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(refreshing: Boolean): SeparatedAdditionGroupList {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val additionGroupList =
            additionGroupRepo.getAdditionGroupList(token = token, refreshing = refreshing)

        return SeparatedAdditionGroupList(
            visibleList =
                additionGroupList
                    .filter { additionGroup ->
                        additionGroup.isVisible
                    }.sortedBy { addition ->
                        addition.name
                    },
            hiddenList =
                additionGroupList
                    .filterNot { additionGroup ->
                        additionGroup.isVisible
                    }.sortedBy { additionGroup ->
                        additionGroup.name
                    },
        )
    }
}
