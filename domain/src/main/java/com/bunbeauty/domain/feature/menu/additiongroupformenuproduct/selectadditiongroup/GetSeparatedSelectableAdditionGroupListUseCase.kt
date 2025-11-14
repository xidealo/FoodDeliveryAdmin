package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.SeparatedAdditionGroupList
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository


// TODO tests
class GetSeparatedSelectableAdditionGroupListUseCase(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase,
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository,
    private val getMenuProductUseCase: GetMenuProductUseCase
) {
    suspend operator fun invoke(
        refreshing: Boolean,
        selectedAdditionGroupUuid: String?,
        menuProductUuid: String
    ): SeparatedAdditionGroupList {
        val separatedAdditionGroupList =
            getSeparatedAdditionGroupListUseCase(refreshing = refreshing)

        val selectedUuid = selectedAdditionGroupUuid?.let {
            menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                uuid = selectedAdditionGroupUuid
            )
        }?.additionGroupUuid ?: selectedAdditionGroupUuid

        // TODO GET ALL ADDITION GROUPS FROM MENU PRODUCT
        val containedAdditionGroupList = getMenuProductUseCase(
            menuProductUuid = menuProductUuid
        ).additionGroups.map { (additionGroup, additionList) ->
            menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                uuid = additionGroup.uuid
            )
        }.map { menuProductToAdditionGroup ->
            menuProductToAdditionGroup?.additionGroupUuid
        }

        return SeparatedAdditionGroupList(
            visibleList = separatedAdditionGroupList.visibleList.filterNot { additionGroup ->
                additionGroup.uuid in containedAdditionGroupList
            },
            hiddenList = separatedAdditionGroupList.hiddenList.filterNot { additionGroup ->
                additionGroup.uuid in containedAdditionGroupList
            }
        )
    }

}
