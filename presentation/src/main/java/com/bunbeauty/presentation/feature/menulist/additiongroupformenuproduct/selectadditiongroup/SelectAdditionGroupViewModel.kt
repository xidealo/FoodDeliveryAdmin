package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.GetSeparatedSelectableAdditionGroupListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class SelectAdditionGroupViewModel(
    private val getSeparatedSelectableAdditionGroupListUseCase: GetSeparatedSelectableAdditionGroupListUseCase,
) : BaseStateViewModel<SelectAdditionGroup.DataState, SelectAdditionGroup.Action, SelectAdditionGroup.Event>(
        initState =
            SelectAdditionGroup.DataState(
                visibleSelectableAdditionGroupList = emptyList(),
                hiddenSelectableAdditionGroupList = emptyList(),
                state = SelectAdditionGroup.DataState.State.LOADING,
            ),
    ) {
    override fun reduce(
        action: SelectAdditionGroup.Action,
        dataState: SelectAdditionGroup.DataState,
    ) {
        when (action) {
            is SelectAdditionGroup.Action.Init ->
                loadData(
                    selectedAdditionGroupUuid = action.selectedAdditionGroupUuid,
                    menuProductUuid = action.menuProductUuid,
                    mainEditedAdditionGroupUuid = action.mainEditedAdditionGroupUuid,
                )

            SelectAdditionGroup.Action.OnBackClick -> backClick()
            is SelectAdditionGroup.Action.SelectAdditionGroupClick ->
                selectAdditionGroupClick(
                    uuid = action.uuid,
                    name = action.name,
                )
        }
    }

    private fun loadData(
        selectedAdditionGroupUuid: String?,
        menuProductUuid: String,
        mainEditedAdditionGroupUuid: String?,
    ) {
        viewModelScope.launchSafe(
            block = {
                val separatedAdditionGroupList =
                    getSeparatedSelectableAdditionGroupListUseCase(
                        refreshing = false,
                        selectedAdditionGroupUuid = selectedAdditionGroupUuid,
                        menuProductUuid = menuProductUuid,
                        mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
                    )
                setState {
                    copy(
                        visibleSelectableAdditionGroupList = separatedAdditionGroupList.visibleList,
                        hiddenSelectableAdditionGroupList = separatedAdditionGroupList.hiddenList,
                        state = SelectAdditionGroup.DataState.State.SUCCESS,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = SelectAdditionGroup.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun backClick() {
        sendEvent {
            SelectAdditionGroup.Event.Back
        }
    }

    private fun selectAdditionGroupClick(
        uuid: String,
        name: String,
    ) {
        sendEvent {
            SelectAdditionGroup.Event.SelectAdditionGroupClicked(
                additionGroupUuid = uuid,
                additionGroupName = name,
            )
        }
    }
}
