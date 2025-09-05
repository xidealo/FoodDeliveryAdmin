package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetAdditionGroupWithAdditionsForMenuUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditAdditionGroupForMenuProductViewModel(
    val getAdditionGroupWithAdditionsForMenuUseCase: GetAdditionGroupWithAdditionsForMenuUseCase
) :
    BaseStateViewModel<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>(
        initState = EditAdditionGroupForMenu.DataState(

        )
    ) {

    override fun reduce(
        action: EditAdditionGroupForMenu.Action,
        dataState: EditAdditionGroupForMenu.DataState
    ) {
        when (action) {
            is EditAdditionGroupForMenu.Action.Init -> loadData(
                additionGroupForMenuUuid = action.additionGroupForMenuUuid
            )

            is EditAdditionGroupForMenu.Action.OnAdditionGroupClick -> onAdditionGroupClick(
                uuid = action.uuid
            )

            EditAdditionGroupForMenu.Action.OnBackClick -> backClick()
        }
    }

    fun loadData(additionGroupForMenuUuid: String) {
        viewModelScope.launchSafe(
            block = {

            },
            onError = {
                // handle error
            }
        )
    }

    fun onAdditionGroupClick(uuid: String) {
        sendEvent {
            EditAdditionGroupForMenu.Event.OnAdditionGroupClick(uuid = uuid)
        }
    }

    fun backClick() {
        sendEvent {
            EditAdditionGroupForMenu.Event.Back
        }
    }
}
