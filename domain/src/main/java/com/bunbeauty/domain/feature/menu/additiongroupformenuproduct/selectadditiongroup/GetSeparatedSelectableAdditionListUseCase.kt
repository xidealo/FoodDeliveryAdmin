package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository

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
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase,
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository
) {
    suspend operator fun invoke(
        refreshing: Boolean,
        selectedAdditionGroupUuid: String?,
    ): SeparatedSelectableAdditionList {
        val separatedAdditionGroupList =
            getSeparatedAdditionGroupListUseCase(refreshing = refreshing)

        val selectedUuid = selectedAdditionGroupUuid?.let {
            menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                uuid = selectedAdditionGroupUuid
            )
        }?.additionGroupUuid ?: selectedAdditionGroupUuid

        return SeparatedSelectableAdditionList(
            visibleList = separatedAdditionGroupList.visibleList.map { additionGroup ->
                additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid = selectedUuid)
            },
            hiddenList = separatedAdditionGroupList.hiddenList.map { additionGroup ->
                additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid = selectedUuid)
            }
        )
    }

    fun AdditionGroup.toSelectableAdditionGroupItem(
        selectedAdditionGroupUuid: String?,
    ): SelectableAdditionGroup {
        return SelectableAdditionGroup(
            uuid = uuid,
            name = name,
            isSelected = selectedAdditionGroupUuid == uuid
        )
    }
}
