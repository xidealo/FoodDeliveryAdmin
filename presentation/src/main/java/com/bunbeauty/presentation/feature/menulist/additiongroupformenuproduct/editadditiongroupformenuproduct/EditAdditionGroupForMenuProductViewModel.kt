package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.SaveEditAdditionGroupWithAdditionsUseCase
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditAdditionGroupForMenuProductViewModel(
    val getFilteredAdditionGroupWithAdditionsForMenuProductUseCase: GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
    val getAdditionListNameUseCase: GetAdditionListNameUseCase,
    val getAdditionGroupUseCase: GetAdditionGroupUseCase,
    val getAdditionUseCase: GetAdditionUseCase,
    val saveEditAdditionGroupWithAdditionsUseCase: SaveEditAdditionGroupWithAdditionsUseCase,
) : BaseStateViewModel<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>(
        initState =
            EditAdditionGroupForMenu.DataState(
                groupName = null,
                state = EditAdditionGroupForMenu.DataState.State.LOADING,
                additionNameList = null,
                isVisible = false,
                additionGroupForMenuProductUuid = "",
                menuProductUuid = "",
                editedAdditionGroupUuid = null,
                editedAdditionListUuid = null,
            ),
    ) {
    override fun reduce(
        action: EditAdditionGroupForMenu.Action,
        dataState: EditAdditionGroupForMenu.DataState,
    ) {
        when (action) {
            is EditAdditionGroupForMenu.Action.Init ->
                loadData(
                    additionGroupForMenuUuid = action.additionGroupForMenuUuid,
                    menuProductUuid = action.menuProductUuid,
                    editedAdditionListUuid = dataState.editedAdditionListUuid,
                )

            is EditAdditionGroupForMenu.Action.OnAdditionGroupClick ->
                onAdditionGroupClick(
                    editedAdditionGroupUuid = dataState.editedAdditionGroupUuid ?: action.uuid,
                menuProductUuid = dataState.menuProductUuid,
                mainEditedAdditionGroupUuid = dataState.additionGroupForMenuProductUuid
            )

            is EditAdditionGroupForMenu.Action.OnAdditionListClick ->
                onAdditionListClick(
                    additionGroupUuid = action.uuid,
                    menuProductUuid = dataState.menuProductUuid,
                    additionGroupName = dataState.groupName.orEmpty(),
                )

            EditAdditionGroupForMenu.Action.OnBackClick -> backClick()

            EditAdditionGroupForMenu.Action.OnSaveClick ->
                saveClick(
                    menuProductToAdditionGroupUuid = dataState.additionGroupForMenuProductUuid,
                    additionGroupUuid = dataState.editedAdditionGroupUuid,
                    additionList = dataState.editedAdditionListUuid,
                )

            is EditAdditionGroupForMenu.Action.SelectAdditionGroup ->
                setSelectedAdditionGroup(
                    action.additionGroupUuid,
                )

            is EditAdditionGroupForMenu.Action.SelectAdditionList ->
                selectAdditionList(
                    additionUuidList = action.additionListUuid,
                )
        }
    }

    private fun saveClick(
        menuProductToAdditionGroupUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>?,
    ) {
        setState {
            copy(
                state = EditAdditionGroupForMenu.DataState.State.LOADING,
            )
        }

        viewModelScope.launchSafe(
            block = {
                saveEditAdditionGroupWithAdditionsUseCase(
                    menuProductToAdditionGroupUuid = menuProductToAdditionGroupUuid,
                    additionGroupUuid = additionGroupUuid,
                    additionList = additionList,
                )

                sendEvent {
                    EditAdditionGroupForMenu.Event.SaveAndBack
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupForMenu.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun loadData(
        menuProductUuid: String,
        additionGroupForMenuUuid: String,
        editedAdditionListUuid: List<String>?,
    ) {
        viewModelScope.launchSafe(
            block = {
                val additionGroupWithAdditionsForMenu =
                    getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
                        menuProductUuid = menuProductUuid,
                        additionGroupForMenuUuid = additionGroupForMenuUuid,
                    )
                setState {
                    copy(
                        additionGroupForMenuProductUuid = additionGroupWithAdditionsForMenu.additionGroup.uuid,
                        groupName = additionGroupWithAdditionsForMenu.additionGroup.name,
                        state = EditAdditionGroupForMenu.DataState.State.SUCCESS,
                        additionNameList =
                            getEditedAdditionUuidList(
                                editedAdditionListUuid = editedAdditionListUuid,
                            ) ?: getAdditionListNameUseCase(
                                additionList = additionGroupWithAdditionsForMenu.additionList,
                            ),
                        isVisible = additionGroupWithAdditionsForMenu.additionGroup.isVisible,
                        menuProductUuid = menuProductUuid,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupForMenu.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun onAdditionGroupClick(
        editedAdditionGroupUuid: String,
        menuProductUuid: String,
        mainEditedAdditionGroupUuid: String
    ) {
        sendEvent {
            EditAdditionGroupForMenu.Event.OnAdditionGroupClick(
                editedAdditionGroupUuid = editedAdditionGroupUuid,
                menuProductUuid = menuProductUuid,
                mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid
            )
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
                additionGroupName = additionGroupName,
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
                        editedAdditionGroupUuid = selectedAdditionGroup.uuid,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupForMenu.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun selectAdditionList(additionUuidList: List<String>) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        additionNameList =
                            getEditedAdditionUuidList(
                                editedAdditionListUuid = additionUuidList,
                            ),
                        editedAdditionListUuid = additionUuidList,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupForMenu.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private suspend fun getEditedAdditionUuidList(editedAdditionListUuid: List<String>?): String? {
        if (editedAdditionListUuid == null) return null
        val additionList =
            editedAdditionListUuid.map { additionUuid ->
                getAdditionUseCase(additionUuid)
            }
        return getAdditionListNameUseCase(
            additionList = additionList,
        )
    }
}
