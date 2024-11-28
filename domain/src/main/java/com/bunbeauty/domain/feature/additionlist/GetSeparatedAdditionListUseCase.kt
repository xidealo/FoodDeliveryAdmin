package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.GetNewUuidUseCase
import javax.inject.Inject

data class SeparatedAdditionList(
    val visibleList: List<GroupedAdditionList>,
    val hiddenList: List<GroupedAdditionList>
)

data class GroupedAdditionList(
    val uuid: String,
    val title: String?,
    val additionList: List<Addition>
)

class GetSeparatedAdditionListUseCase @Inject constructor(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val getNewUuidUseCase: GetNewUuidUseCase
) {
    suspend operator fun invoke(refreshing: Boolean): SeparatedAdditionList {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val additionList = additionRepo.getAdditionList(token = token, refreshing = refreshing)

        return SeparatedAdditionList(
            visibleList = additionList
                .filter { addition ->
                    addition.isVisible
                }
                .groupBy { addition ->
                    addition.tag
                }.map { mapOfTagAndAdditionList ->
                    GroupedAdditionList(
                        uuid = getNewUuidUseCase(),
                        title = mapOfTagAndAdditionList.key,
                        additionList = getSortedAdditionList(
                            additionList = mapOfTagAndAdditionList.value
                        )
                    )
                } // set elements with title == null to the end of list
                .sortedWith(
                    compareBy<GroupedAdditionList> { groupedAdditionList ->
                        groupedAdditionList.title == null
                    }.thenBy { groupedAdditionList ->
                        groupedAdditionList.title
                    }
                ),
            hiddenList = additionList
                .filterNot { addition ->
                    addition.isVisible
                }
                .groupBy { addition ->
                    addition.tag
                }.map { mapOfTagAndAdditionList ->
                    GroupedAdditionList(
                        uuid = getNewUuidUseCase(),
                        title = mapOfTagAndAdditionList.key,
                        additionList = getSortedAdditionList(
                            additionList = mapOfTagAndAdditionList.value
                        )
                    )
                } // set elements with title == null to the end of list
                .sortedWith(
                    compareBy<GroupedAdditionList> { groupedAdditionList ->
                        groupedAdditionList.title == null
                    }.thenBy { groupedAdditionList ->
                        groupedAdditionList.title
                    }
                )
        )
    }

    private fun getSortedAdditionList(additionList: List<Addition>): List<Addition> {
        return additionList.sortedBy { addition ->
            addition.name
        }
    }
}
