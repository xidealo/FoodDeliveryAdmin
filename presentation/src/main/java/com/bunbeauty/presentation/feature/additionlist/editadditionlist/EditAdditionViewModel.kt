package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditAdditionViewModel(
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase,
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState = EditAddition.DataState(
        uuid = "",
        name = "",
        priority = null,
        prise = null,
        isLoading = false,
        isVisible = false,
        fullName = "",
        hasFullNameError = false,
        hasNameError = false
    )
) {
    override fun reduce(action: EditAddition.Action, dataState: EditAddition.DataState) {
        when (action) {
            EditAddition.Action.OnBackClick -> addEvent { EditAddition.Event.Back }
            EditAddition.Action.Init -> loadData(dataState.uuid)
            is EditAddition.Action.OnVisibleClick -> updateVisible(
                isVisible = action.isVisible
            )

            EditAddition.Action.SaveEditAdditionClick -> updateEditAddition()
            EditAddition.Action.FullName -> onFullNameTextChanged(
                fullName = dataState.fullName
            )

            EditAddition.Action.Name -> onNameTextChanged(
                name = dataState.name
            )

            EditAddition.Action.Priority -> onPriorityTextChanged(
                priority = dataState.priority
            )

            EditAddition.Action.Prise -> onPriseTextChanged(
                prise = dataState.prise
            )
        }
    }

    private fun onNameTextChanged(name: String) {
        setState {
            copy(name = name)
        }
    }

    private fun onPriorityTextChanged(priority: Int?) {
        setState {
            copy(priority = priority)
        }
    }

    private fun onPriseTextChanged(prise: Int?) {
        setState {
            copy(prise = prise)
        }
    }

    private fun onFullNameTextChanged(fullName: String?) {
        setState {
            copy(fullName = fullName)
        }
    }

    private fun updateVisible(isVisible: Boolean) {
        setState {
            copy(isVisible = isVisible)
        }
    }

    private fun updateEditAddition() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isLoading = isLoading,
                        hasNameError = hasNameError,
                        hasFullNameError = hasFullNameError
                    )
                }
                with(state.value) {
                    updateAdditionUseCase(
                        additionUuid = uuid,
                        isVisible = isVisible,
                        updateAddition = UpdateAddition(
                            name = name.trim(),
                            priority = priority,
                            fullName = fullName,
                            price = prise,
                        ),
                    )
                }

            },
            onError = {}
        )
    }

    private fun loadData(additionUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val addition = getAdditionUseCase(additionUuid)
                setState {
                    copy(
                        name = addition.name,
                        priority = addition.priority,
                        fullName = addition.fullName,
                        prise = addition.price,
                        isVisible = addition.isVisible,
                    )
                }
            },
            onError = {}
        )
    }
}




