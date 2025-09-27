package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition.GetSelectedAdditionListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class SelectAdditionListViewModel(
    val getSelectedAdditionListUseCase: GetSelectedAdditionListUseCase
) :
    BaseStateViewModel<SelectAdditionList.DataState, SelectAdditionList.Action, SelectAdditionList.Event>(
        initState = SelectAdditionList.DataState(
            state = SelectAdditionList.DataState.State.LOADING,
            groupName = "",
            selectedAdditionList = emptyList(),
            notSelectedAdditionList = emptyList()
        )
    ) {

    override fun reduce(
        action: SelectAdditionList.Action,
        dataState: SelectAdditionList.DataState
    ) {
        when (action) {
            is SelectAdditionList.Action.Init -> loadData(
                selectedAdditionUuid = action.additionGroupUuid,
                menuProductUuid = action.menuProductUuid
            )

            SelectAdditionList.Action.OnBackClick -> backClick()
            is SelectAdditionList.Action.SelectAdditionClick -> {}
        }
    }

    private fun loadData(
        selectedAdditionUuid: String?,
        menuProductUuid: String
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = SelectAdditionList.DataState.State.SUCCESS
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = SelectAdditionList.DataState.State.ERROR
                    )
                }
            }
        )
    }

    private fun backClick() {
        sendEvent {
            SelectAdditionList.Event.Back
        }
    }

    private fun selectAdditionGroupClick(uuid: String, name: String) {
    }
}
