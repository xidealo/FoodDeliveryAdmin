package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateEditAdditionGroupWithAdditionsUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenu
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionGroupForMenuProductViewModel(
    private val createEditAdditionGroupWithAdditionsUseCase: CreateEditAdditionGroupWithAdditionsUseCase,
    private val getAdditionGroupUseCase: GetAdditionGroupUseCase,
) :
    BaseStateViewModel<CreateAdditionGroupForMenu.DataState, CreateAdditionGroupForMenu.Action, CreateAdditionGroupForMenu.Event>(
        initState = CreateAdditionGroupForMenu.DataState(
            state = CreateAdditionGroupForMenu.DataState.State.SUCCESS,
            groupName = null,
            additionNameList = null,
            menuProductUuid = "",
            editedAdditionGroupUuid = null,
            additionGroupForMenuProductUuid = "",
            editedAdditionListUuid = listOf(),
            isVisible = false,
        )
    ) {

    override fun reduce(
        action: CreateAdditionGroupForMenu.Action,
        dataState: CreateAdditionGroupForMenu.DataState
    ) {
        when (action) {
            is CreateAdditionGroupForMenu.Action.SelectAdditionGroup -> {
                setSelectedAdditionGroup(action.additionGroupUuid)
            }

            is CreateAdditionGroupForMenu.Action.SelectAdditionList -> {

            }

            CreateAdditionGroupForMenu.Action.OnAdditionGroupClick -> {
                sendEvent {
                    CreateAdditionGroupForMenu.Event.OnAdditionGroupClick(
                        uuid = dataState.editedAdditionGroupUuid.orEmpty()
                    )
                }
            }

            CreateAdditionGroupForMenu.Action.OnAdditionListClick -> {
                sendEvent {
                    CreateAdditionGroupForMenu.Event.OnAdditionListClick(
                        additionGroupUuid = dataState.editedAdditionGroupUuid.orEmpty(),
                        menuProductUuid = dataState.menuProductUuid,
                        additionGroupName = dataState.groupName.orEmpty(),
                    )
                }
            }

            CreateAdditionGroupForMenu.Action.OnSaveClick -> {
                viewModelScope.launchSafe(
                    block = {
                        createEditAdditionGroupWithAdditionsUseCase(
                            menuProductUuid = dataState.menuProductUuid,
                            additionGroupUuid = dataState.editedAdditionGroupUuid,
                            additionList = dataState.editedAdditionListUuid
                        )
                    },
                    onError = { error ->
                        when (error) {
                            is NoAdditionGroupException -> {
                                // TODO
                                print("No")
                            }
                        }
                    }
                )
            }

            CreateAdditionGroupForMenu.Action.OnBackClick -> backClick()
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
                        state = CreateAdditionGroupForMenu.DataState.State.ERROR
                    )
                }
            }
        )
    }

    private fun backClick() {
        sendEvent { CreateAdditionGroupForMenu.Event.Back }
    }

    private fun selectAdditionList(additionUuidList: List<String>) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        additionNameList = getEditedAdditionUuidList(
                            editedAdditionListUuid = additionUuidList
                        ),
                        editedAdditionListUuid = additionUuidList
                    )
                }
            },
            onError = {
                // set error
            }
        )
    }
}
