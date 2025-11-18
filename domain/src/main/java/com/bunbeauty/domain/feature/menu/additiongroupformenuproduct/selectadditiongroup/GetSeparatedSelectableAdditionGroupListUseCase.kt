package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository

data class SeparatedSelectableAdditionList(
    val visibleList: List<SelectableAdditionGroup>,
    val hiddenList: List<SelectableAdditionGroup>,
)

data class SelectableAdditionGroup(
    val uuid: String,
    val name: String,
    val isSelected: Boolean,
)

// TODO tests
class GetSeparatedSelectableAdditionGroupListUseCase(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase,
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository,
    private val getMenuProductUseCase: GetMenuProductUseCase,
) {
    suspend operator fun invoke(
        refreshing: Boolean,
        selectedAdditionGroupUuid: String?,
        menuProductUuid: String,
        mainEditedAdditionGroupUuid: String?,
    ): SeparatedSelectableAdditionList {
        val separatedAdditionGroupList =
            getSeparatedAdditionGroupListUseCase(refreshing = refreshing)

        val selectedUuid = getSelectedAdditionGroupUuid(selectedAdditionGroupUuid)

        val mainAdditionGroupUuid =
            getMainAdditionGroupUuid(
                mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
            )

        val containedAdditionGroupList =
            getContainedAdditionGroupUuidList(
                menuProductUuid = menuProductUuid,
            )

        return SeparatedSelectableAdditionList(
            visibleList =
                separatedAdditionGroupList.visibleList
                    .filterNot { additionGroup ->
                        additionGroup.uuid in containedAdditionGroupList && mainAdditionGroupUuid != additionGroup.uuid
                    }.map { additionGroup ->
                        additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid = selectedUuid)
                    },
            hiddenList =
                separatedAdditionGroupList.hiddenList
                    .filterNot { additionGroup ->
                        additionGroup.uuid in containedAdditionGroupList && mainAdditionGroupUuid != additionGroup.uuid
                    }.map { additionGroup ->
                        additionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid = selectedUuid)
                    },
        )
    }

    private suspend fun getSelectedAdditionGroupUuid(selectedAdditionGroupUuid: String?): String? =
        selectedAdditionGroupUuid
            ?.let {
                menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                    uuid = selectedAdditionGroupUuid,
                )
            }?.additionGroupUuid ?: selectedAdditionGroupUuid

    private suspend fun getMainAdditionGroupUuid(mainEditedAdditionGroupUuid: String?): String? =
        mainEditedAdditionGroupUuid
            ?.let {
                menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                    uuid = mainEditedAdditionGroupUuid,
                )
            }?.additionGroupUuid

    private suspend fun getContainedAdditionGroupUuidList(menuProductUuid: String): List<String> =
        getMenuProductUseCase(
            menuProductUuid = menuProductUuid,
        ).additionGroups
            .mapNotNull { (additionGroup, additionList) ->
                menuProductToAdditionGroupRepository.getMenuProductToAdditionGroup(
                    uuid = additionGroup.uuid,
                )
            }.map { menuProductToAdditionGroup ->
                menuProductToAdditionGroup.additionGroupUuid
            }

    fun AdditionGroup.toSelectableAdditionGroupItem(selectedAdditionGroupUuid: String?): SelectableAdditionGroup =
        SelectableAdditionGroup(
            uuid = uuid,
            name = name,
            isSelected = selectedAdditionGroupUuid == uuid,
        )
}
