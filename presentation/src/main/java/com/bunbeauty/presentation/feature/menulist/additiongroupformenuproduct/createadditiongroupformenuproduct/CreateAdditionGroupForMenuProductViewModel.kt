package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.exception.NoAdditionListException
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateEditAdditionGroupWithAdditionsUseCase
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionGroupForMenuProductViewModel(
    private val createEditAdditionGroupWithAdditionsUseCase: CreateEditAdditionGroupWithAdditionsUseCase,
    private val getAdditionGroupUseCase: GetAdditionGroupUseCase,
    val getAdditionListNameUseCase: GetAdditionListNameUseCase,
    val getAdditionUseCase: GetAdditionUseCase,
) : BaseStateViewModel<CreateAdditionGroupForMenu.DataState, CreateAdditionGroupForMenu.Action, CreateAdditionGroupForMenu.Event>(
        initState =
            CreateAdditionGroupForMenu.DataState(
                state = CreateAdditionGroupForMenu.DataState.State.SUCCESS,
                groupName = null,
                additionNameList = null,
                menuProductUuid = "",
                editedAdditionGroupUuid = null,
                additionGroupForMenuProductUuid = "",
                editedAdditionListUuid = listOf(),
                isVisible = false,
                groupHasError = false,
                isSaveLoading = false,
                additionListHasError = false,
            ),
    ) {
    override fun reduce(
        action: CreateAdditionGroupForMenu.Action,
        dataState: CreateAdditionGroupForMenu.DataState,
    ) {
        when (action) {
            is CreateAdditionGroupForMenu.Action.Init ->
                initData(
                    menuProductUuid = action.menuProductUuid,
                )

            is CreateAdditionGroupForMenu.Action.SelectAdditionGroup -> {
                setSelectedAdditionGroup(action.additionGroupUuid)
            }

            is CreateAdditionGroupForMenu.Action.SelectAdditionList ->
                selectAdditionList(
                    additionUuidList = action.additionListUuid,
                )

            CreateAdditionGroupForMenu.Action.OnAdditionGroupClick ->
                onAdditionGroupClick(
                    uuid = dataState.editedAdditionGroupUuid.orEmpty(),
                    menuProductUuid = dataState.menuProductUuid,
                )

            CreateAdditionGroupForMenu.Action.OnAdditionListClick ->
                onAdditionListClick(
                    menuProductUuid = dataState.menuProductUuid,
                    groupName = dataState.groupName.orEmpty(),
                )

            CreateAdditionGroupForMenu.Action.OnBackClick -> backClick()

            CreateAdditionGroupForMenu.Action.OnSaveClick ->
                onSaveClick(
                    menuProductUuid = dataState.menuProductUuid,
                    editedAdditionGroupUuid = dataState.editedAdditionGroupUuid,
                    editedAdditionListUuid = dataState.editedAdditionListUuid.orEmpty(),
                )
        }
    }

    private fun initData(menuProductUuid: String) {
        setState {
            copy(
                menuProductUuid = menuProductUuid,
            )
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
                        groupHasError = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = CreateAdditionGroupForMenu.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun onAdditionGroupClick(
        menuProductUuid: String,
        uuid: String,
    ) {
        sendEvent {
            CreateAdditionGroupForMenu.Event.OnAdditionGroupClick(
                uuid = uuid,
                menuProductUuid = menuProductUuid,
            )
        }
    }

    private fun onAdditionListClick(
        menuProductUuid: String,
        groupName: String,
    ) {
        sendEvent {
            CreateAdditionGroupForMenu.Event.OnAdditionListClick(
                menuProductUuid = menuProductUuid,
                additionGroupName = groupName.orEmpty(),
            )
        }
    }

    private fun backClick() {
        sendEvent { CreateAdditionGroupForMenu.Event.Back }
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
                        additionListHasError = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = CreateAdditionGroupForMenu.DataState.State.ERROR,
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

    private fun onSaveClick(
        menuProductUuid: String,
        editedAdditionGroupUuid: String?,
        editedAdditionListUuid: List<String>,
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isSaveLoading = true,
                    )
                }

                createEditAdditionGroupWithAdditionsUseCase(
                    menuProductUuid = menuProductUuid,
                    additionGroupUuid = editedAdditionGroupUuid,
                    additionList = editedAdditionListUuid,
                )

                sendEvent {
                    CreateAdditionGroupForMenu.Event.SaveAndBack
                }
            },
            onError = { error ->
                when (error) {
                    is NoAdditionGroupException -> {
                        setState {
                            copy(
                                groupHasError = true,
                                isSaveLoading = false,
                            )
                        }
                    }

                    is NoAdditionListException -> {
                        setState {
                            copy(
                                additionListHasError = true,
                                isSaveLoading = false,
                            )
                        }
                    }

                    else -> {
                        setState {
                            copy(
                                state = CreateAdditionGroupForMenu.DataState.State.ERROR,
                                isSaveLoading = false,
                            )
                        }
                    }
                }
            },
        )
    }
}
