package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetAdditionGroupWithAdditionsForMenuUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditAdditionGroupForMenuProductViewModel(
    val getAdditionGroupWithAdditionsForMenuUseCase: GetAdditionGroupWithAdditionsForMenuUseCase,
    val getAdditionListNameUseCase: GetAdditionListNameUseCase
) :
    BaseStateViewModel<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>(
        initState = EditAdditionGroupForMenu.DataState(
            groupName = "",
            state = EditAdditionGroupForMenu.DataState.State.LOADING,
            additionNameList = null,
            isVisible = false,
            additionGroupUuid = ""
        )
    ) {

    override fun reduce(
        action: EditAdditionGroupForMenu.Action,
        dataState: EditAdditionGroupForMenu.DataState
    ) {
        when (action) {
            is EditAdditionGroupForMenu.Action.Init -> loadData(
                additionGroupForMenuUuid = action.additionGroupForMenuUuid,
                menuProductUuid = action.menuProductUuid
            )

            is EditAdditionGroupForMenu.Action.OnAdditionGroupClick -> onAdditionGroupClick(
                uuid = action.uuid
            )

            EditAdditionGroupForMenu.Action.OnBackClick -> backClick()
        }
    }

    private fun loadData(menuProductUuid: String, additionGroupForMenuUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val additionGroupWithAdditionsForMenu =
                    getAdditionGroupWithAdditionsForMenuUseCase(
                        menuProductUuid = menuProductUuid,
                        additionGroupForMenuUuid = additionGroupForMenuUuid
                    )
                setState {
                    copy(
                        additionGroupUuid = additionGroupWithAdditionsForMenu.additionGroup.uuid,
                        groupName = additionGroupWithAdditionsForMenu.additionGroup.name,
                        state = EditAdditionGroupForMenu.DataState.State.SUCCESS,
                        additionNameList = getAdditionListNameUseCase(
                            additionList = additionGroupWithAdditionsForMenu.additionList
                        ),
                        isVisible = additionGroupWithAdditionsForMenu.additionGroup.isVisible
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupForMenu.DataState.State.ERROR
                    )
                }
            }
        )
    }

    private fun onAdditionGroupClick(uuid: String) {
        sendEvent {
            EditAdditionGroupForMenu.Event.OnAdditionGroupClick(uuid = uuid)
        }
    }

    private fun backClick() {
        sendEvent {
            EditAdditionGroupForMenu.Event.Back
        }
    }
}
