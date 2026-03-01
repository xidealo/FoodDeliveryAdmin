package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.CreateAdditionGroupUseCase
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionGroupViewModel(
    private val createAdditionGroupUseCase: CreateAdditionGroupUseCase,
) : BaseStateViewModel<CreateAdditionGroupDataState.DataState, CreateAdditionGroupDataState.Action, CreateAdditionGroupDataState.Event>(
        initState =
            CreateAdditionGroupDataState.DataState(
                state = CreateAdditionGroupDataState.DataState.State.SUCCESS,
                isLoading = false,
                nameField = TextFieldData.empty,
                isShowMenuVisible = false,
                singleChoice = false,
                nameStateError = CreateAdditionGroupDataState.DataState.NameStateError.NO_ERROR,
            ),
    ) {
    override fun reduce(
        action: CreateAdditionGroupDataState.Action,
        dataState: CreateAdditionGroupDataState.DataState,
    ) {
        when (action) {
            CreateAdditionGroupDataState.Action.OnBackClick -> onBackClicked()
            CreateAdditionGroupDataState.Action.OnErrorStateClicked -> onErrorState()
            CreateAdditionGroupDataState.Action.OnSaveAdditionGroupClick ->
                saveAdditionGroup(
                    additionGroupName = dataState.nameField.value,
                    isVisible = dataState.isShowMenuVisible,
                    singleChoice = dataState.singleChoice,
                )

            is CreateAdditionGroupDataState.Action.OnOneAdditionVisibleClick -> onOneAdditionVisibleClick()

            is CreateAdditionGroupDataState.Action.OnVisibleClick -> onVisibleClick()
            is CreateAdditionGroupDataState.Action.CreateNameAdditionGroupChanged ->
                createNameAdditionGroup(
                    additionGroupName = action.nameGroup,
                )
        }
    }

    private fun createNameAdditionGroup(additionGroupName: String) {
        setState {
            copy(
                nameField =
                    nameField.copy(
                        value = additionGroupName,
                        isError = false,
                    ),
            )
        }
    }

    private fun saveAdditionGroup(
        additionGroupName: String,
        isVisible: Boolean,
        singleChoice: Boolean,
    ) {
        setState {
            copy(
                nameField = nameField.copy(isError = false),
            )
        }
        viewModelScope.launchSafe(
            block = {
                createAdditionGroupUseCase(
                    additionName = additionGroupName,
                    isVisible = isVisible,
                    singleChoice = singleChoice,
                )
                setState { copy(isLoading = false) }
                sendEvent {
                    CreateAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess(
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
                                    CreateAdditionGroupDataState
                                        .DataState.NameStateError.EMPTY_NAME,
                                nameField =
                                    nameField.copy(
                                        isError = true,
                                    ),
                                isLoading = false,
                            )
                        }

                        is DuplicateAdditionGroupNameException -> {
                            copy(
                                nameStateError =
                                    CreateAdditionGroupDataState
                                        .DataState.NameStateError.DUPLICATE_NAME,
                                nameField =
                                    nameField.copy(
                                        isError = true,
                                    ),
                                isLoading = false,
                            )
                        }

                        else ->
                            copy(
                                isLoading = false,
                                nameStateError =
                                    CreateAdditionGroupDataState
                                        .DataState.NameStateError.NO_ERROR,
                            )
                    }
                }
            },
        )
    }

    private fun onVisibleClick() {
        setState {
            copy(isShowMenuVisible = !isShowMenuVisible)
        }
    }

    private fun onOneAdditionVisibleClick() {
        setState {
            copy(singleChoice = !singleChoice)
        }
    }

    private fun onErrorState() {
        setState {
            copy(
                state = CreateAdditionGroupDataState.DataState.State.SUCCESS,
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            CreateAdditionGroupDataState.Event.GoBackEvent
        }
    }
}
