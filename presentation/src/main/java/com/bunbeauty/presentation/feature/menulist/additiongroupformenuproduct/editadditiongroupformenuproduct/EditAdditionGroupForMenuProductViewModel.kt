package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditAdditionGroupForMenuProductViewModel(
    val getAdditionGroupWithAdditionsForMenuProductUseCase: GetAdditionGroupWithAdditionsForMenuProductUseCase,
    val getAdditionListNameUseCase: GetAdditionListNameUseCase,
    val getAdditionGroupUseCase: GetAdditionGroupUseCase
) :
    BaseStateViewModel<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>(
        initState = EditAdditionGroupForMenu.DataState(
            groupName = "",
            state = EditAdditionGroupForMenu.DataState.State.LOADING,
            additionNameList = null,
            isVisible = false,
            additionGroupForMenuProductUuid = "",
            menuProductUuid = "",
            editedAdditionGroupUuid = null
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
                uuid = dataState.editedAdditionGroupUuid ?: action.uuid
            )

            is EditAdditionGroupForMenu.Action.OnAdditionListClick -> onAdditionListClick(
                additionGroupUuid = action.uuid,
                menuProductUuid = dataState.menuProductUuid,
                additionGroupName = dataState.groupName
            )

            EditAdditionGroupForMenu.Action.OnBackClick

                -> backClick()

            EditAdditionGroupForMenu.Action.OnSaveClick -> saveClick()
            is EditAdditionGroupForMenu.Action.SelectAdditionGroup -> setSelectedAdditionGroup(
                action.additionGroupUuid
            )
        }
    }

    private fun saveClick() {
        setState {
            copy(
                state = EditAdditionGroupForMenu.DataState.State.LOADING
            )
        }

        sendEvent {
            EditAdditionGroupForMenu.Event.Back
        }
    }

    private fun loadData(menuProductUuid: String, additionGroupForMenuUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val additionGroupWithAdditionsForMenu =
                    getAdditionGroupWithAdditionsForMenuProductUseCase(
                        menuProductUuid = menuProductUuid,
                        additionGroupForMenuUuid = additionGroupForMenuUuid
                    )
                setState {
                    copy(
                        additionGroupForMenuProductUuid = additionGroupWithAdditionsForMenu.additionGroup.uuid,
                        groupName = additionGroupWithAdditionsForMenu.additionGroup.name,
                        state = EditAdditionGroupForMenu.DataState.State.SUCCESS,
                        additionNameList = getAdditionListNameUseCase(
                            additionList = additionGroupWithAdditionsForMenu.additionList
                        ),
                        isVisible = additionGroupWithAdditionsForMenu.additionGroup.isVisible,
                        menuProductUuid = menuProductUuid
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

    private fun onAdditionListClick(
        additionGroupUuid: String,
        additionGroupName: String,
        menuProductUuid: String,
    ) {
        sendEvent {
            EditAdditionGroupForMenu.Event.OnAdditionListClick(
                additionGroupUuid = additionGroupUuid,
                menuProductUuid = menuProductUuid,
                additionGroupName = additionGroupName
            )
        }
    }

    private fun backClick() {
        sendEvent {
            EditAdditionGroupForMenu.Event.Back
        }
    }

    private fun setSelectedAdditionGroup(uuid: String) {
        viewModelScope.launchSafe(
            block = {
                val selectedAdditionGroup =
                    getAdditionGroupUseCase(additionGroupUuid = uuid)

                setState {
                    copy(
                        groupName = selectedAdditionGroup.name,
                        editedAdditionGroupUuid = selectedAdditionGroup.uuid
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
}
