package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.CreateAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionGroupListViewModel(
    private val createAdditionGroupListUseCase: CreateAdditionGroupListUseCase
) : BaseStateViewModel<CreateAdditionGroupListDataState.DataState, CreateAdditionGroupListDataState.Action, CreateAdditionGroupListDataState.Event>(
    initState = CreateAdditionGroupListDataState.DataState(
        state = CreateAdditionGroupListDataState.DataState.State.SUCCESS,
        isLoading = false,
        nameField = TextFieldData.empty,
        isShowMenuVisible = false,
        singleChoice = false,
        nameStateError = CreateAdditionGroupListDataState.DataState.NameStateError.NO_ERROR
    )
) {
    override fun reduce(
        action: CreateAdditionGroupListDataState.Action,
        dataState: CreateAdditionGroupListDataState.DataState
    ) {
        when (action) {
            CreateAdditionGroupListDataState.Action.OnBackClick -> onBackClicked()
            CreateAdditionGroupListDataState.Action.OnErrorStateClicked -> onErrorState()
            CreateAdditionGroupListDataState.Action.OnSaveAdditionGroupListClick -> saveAdditionGroup(
                additionGroupName = dataState.nameField.value,
                isVisible = dataState.isShowMenuVisible,
                singleChoice = dataState.singleChoice
            )

            is CreateAdditionGroupListDataState.Action.OnOneAdditionVisibleClick -> onOneAdditionVisibleClick()

            is CreateAdditionGroupListDataState.Action.OnVisibleClick -> onVisibleClick()
            is CreateAdditionGroupListDataState.Action.CreateNameAdditionGroupListChanged -> createNameAdditionGroup(
                additionGroupName = action.nameGroup
            )
        }
    }

    private fun createNameAdditionGroup(additionGroupName: String) {
        setState {
            copy(
                nameField = nameField.copy(
                    value = additionGroupName,
                    isError = false
                )
            )
        }
    }

    private fun saveAdditionGroup(
        additionGroupName: String,
        isVisible: Boolean,
        singleChoice: Boolean
    ) {
        setState {
            copy(
                nameField = nameField.copy(isError = false)
            )
        }
        viewModelScope.launchSafe(
            block = {
                createAdditionGroupListUseCase(
                    additionName = additionGroupName,
                    isVisible = isVisible,
                    singleChoice = singleChoice
                )
                setState { copy(isLoading = false) }
                sendEvent {
                    CreateAdditionGroupListDataState.Event.ShowUpdateAdditionGroupSuccess(
                        additionGroupName = additionGroupName
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is AdditionGroupNameException -> {
                            copy(
                                nameStateError = CreateAdditionGroupListDataState
                                    .DataState.NameStateError.EMPTY_NAME,
                                nameField = nameField.copy(
                                    isError = true
                                ),
                                isLoading = false
                            )
                        }

                        is DuplicateAdditionGroupNameException -> {
                            copy(
                                nameStateError = CreateAdditionGroupListDataState
                                    .DataState.NameStateError.DUPLICATE_NAME,
                                nameField = nameField.copy(
                                    isError = true
                                ),
                                isLoading = false
                            )
                        }

                        else -> copy(
                            isLoading = false,
                            nameStateError = CreateAdditionGroupListDataState
                                .DataState.NameStateError.NO_ERROR
                        )
                    }
                }
            }
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
                state = CreateAdditionGroupListDataState.DataState.State.SUCCESS
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            CreateAdditionGroupListDataState.Event.GoBackEvent
        }
    }
}
