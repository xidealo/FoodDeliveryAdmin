package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import androidx.lifecycle.viewModelScope
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class SelectAdditionGroupViewModel() :
    BaseStateViewModel<SelectAdditionGroup.DataState, SelectAdditionGroup.Action, SelectAdditionGroup.Event>(
        initState = SelectAdditionGroup.DataState(
            selectableAdditionGroupList = emptyList(),
            state = SelectAdditionGroup.DataState.State.LOADING
        )
    ) {

    override fun reduce(
        action: SelectAdditionGroup.Action,
        dataState: SelectAdditionGroup.DataState
    ) {
        when (action) {
            is SelectAdditionGroup.Action.Init -> loadData(
                selectedAdditionGroupUuid = action.selectedAdditionGroupUuid,
            )

            SelectAdditionGroup.Action.OnBackClick -> backClick()
        }
    }

    fun loadData(selectedAdditionGroupUuid: String?) {
        viewModelScope.launchSafe(
            block = {

            },
            onError = {

            }
        )
    }

    fun backClick() {
        sendEvent {
            SelectAdditionGroup.Event.Back
        }
    }
}
