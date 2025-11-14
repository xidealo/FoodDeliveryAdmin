package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.EditAdditionGroupUseCase
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val ADDITION_GROUP_UUID = "additionGroupUuid"
private const val PRIORITY_ADDITION_GROUP = 1

class EditAdditionGroupViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAdditionGroupUseCase: GetAdditionGroupUseCase,
    private val saveEditAdditionGroupUseCase: EditAdditionGroupUseCase,
) : BaseStateViewModel<EditAdditionGroupDataState.DataState, EditAdditionGroupDataState.Action, EditAdditionGroupDataState.Event>(
        initState =
            EditAdditionGroupDataState.DataState(
                uuid = "",
                name = TextFieldData.empty,
                isLoading = false,
                state = EditAdditionGroupDataState.DataState.State.SUCCESS,
                nameStateError = EditAdditionGroupDataState.DataState.NameStateError.NO_ERROR,
                isVisible = true,
                isSingleChoice = true,
            ),
    ) {
    override fun reduce(
        action: EditAdditionGroupDataState.Action,
        dataState: EditAdditionGroupDataState.DataState,
    ) {
        when (action) {
            EditAdditionGroupDataState.Action.Init -> loadData()
            EditAdditionGroupDataState.Action.OnBackClicked -> onBackClicked()
            is EditAdditionGroupDataState.Action.EditNameAdditionGroup ->
                editNameAddition(
                    nameEditAddition = action.nameEditAdditionGroup,
                )

            EditAdditionGroupDataState.Action.OnSaveEditAdditionGroupClick ->
                saveEditCategory(
                    additionGroupUuid = dataState.uuid,
                    additionGroupName = dataState.name.value,
                    isVisible = dataState.isVisible,
                    isVisibleSingleChoice = dataState.isSingleChoice,
                )
            is EditAdditionGroupDataState.Action.OnVisibleMenu -> onVisibleMenu(action = action)
            is EditAdditionGroupDataState.Action.OnVisibleSingleChoice ->
                onVisibleSingleChoice(
                    action = action,
                )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            EditAdditionGroupDataState.Event.GoBackEvent
        }
    }

    private fun editNameAddition(nameEditAddition: String) {
        setState {
            copy(
                name =
                    name.copy(
                        value = nameEditAddition,
                        isError = false,
                    ),
            )
        }
    }

    private fun onVisibleMenu(action: EditAdditionGroupDataState.Action.OnVisibleMenu) {
        setState {
            copy(isVisible = action.isVisible)
        }
    }

    private fun onVisibleSingleChoice(action: EditAdditionGroupDataState.Action.OnVisibleSingleChoice) {
        setState {
            copy(isSingleChoice = action.isVisibleSingleChoice)
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val additionGroupUuidNavigation =
                    savedStateHandle.get<String>(ADDITION_GROUP_UUID).orEmpty()
                val additionGroup =
                    getAdditionGroupUseCase(additionGroupUuid = additionGroupUuidNavigation)
                setState {
                    copy(
                        uuid = additionGroup.uuid,
                        isSingleChoice = additionGroup.singleChoice,
                        isVisible = additionGroup.isVisible,
                        name =
                            name.copy(
                                value = additionGroup.name,
                                isError = false,
                            ),
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = EditAdditionGroupDataState.DataState.State.ERROR,
                    )
                }
            },
        )
    }

    private fun saveEditCategory(
        additionGroupUuid: String,
        additionGroupName: String,
        isVisible: Boolean,
        isVisibleSingleChoice: Boolean,
    ) {
        viewModelScope.launchSafe(
            block = {
                saveEditAdditionGroupUseCase(
                    additionGroupUuid = additionGroupUuid,
                    updateAdditionGroup =
                        state.value.run {
                            UpdateAdditionGroup(
                                name = additionGroupName.trim(),
                                isVisible = isVisible,
                                singleChoice = isVisibleSingleChoice,
                                priority = PRIORITY_ADDITION_GROUP,
                            )
                        },
                )
                setState {
                    copy(isLoading = false)
                }

                sendEvent {
                    EditAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess(
                        additionGroupName = additionGroupName,
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is AdditionGroupNameException -> {
                            copy(
                                nameStateError =
                                    EditAdditionGroupDataState
                                        .DataState.NameStateError.EMPTY_NAME,
                                name =
                                    name.copy(
                                        isError = true,
                                    ),
                                isLoading = false,
                            )
                        }

                        is DuplicateAdditionGroupNameException -> {
                            copy(
                                nameStateError =
                                    EditAdditionGroupDataState
                                        .DataState.NameStateError.DUPLICATE_NAME,
                                name =
                                    name.copy(
                                        isError = true,
                                    ),
                                isLoading = false,
                            )
                        }

                        else ->
                            copy(
                                isLoading = false,
                                nameStateError =
                                    EditAdditionGroupDataState
                                        .DataState.NameStateError.NO_ERROR,
                            )
                    }
                }
            },
        )
    }
}
