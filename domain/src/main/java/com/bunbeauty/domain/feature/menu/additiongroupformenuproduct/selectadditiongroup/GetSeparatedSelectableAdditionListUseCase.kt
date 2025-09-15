package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroup

data class SeparatedSelectableAdditionList(
    val visibleList: List<SelectableAdditionGroup>,
    val hiddenList: List<SelectableAdditionGroup>
)

data class SelectableAdditionGroup(
    val uuid: String,
    val name: String,
    val isSelected: Boolean
)

// TODO tests
class GetSeparatedSelectableAdditionListUseCase(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase
) {
    suspend operator fun invoke(
        refreshing: Boolean,
        selectedAdditionGroupUuid: String?
    ): SeparatedSelectableAdditionList {
        val separatedAdditionGroupList =
            getSeparatedAdditionGroupListUseCase(refreshing = refreshing)

        return SeparatedSelectableAdditionList(
            visibleList = separatedAdditionGroupList.visibleList.map { additionGroup ->
                additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid)
            },
            hiddenList = separatedAdditionGroupList.hiddenList.map { additionGroup ->
                additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid)
            }
        )
    }

    fun AdditionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid: String?): SelectableAdditionGroup {
        return SelectableAdditionGroup(
            uuid = uuid,
            name = name,
            isSelected = uuid == selectedAdditionGroupUuid
        )
    }
}
