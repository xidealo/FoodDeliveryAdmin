package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateaddition.AdditionFullNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriceException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val ADDITION_UUID = "additionUuid"

@HiltViewModel
class EditAdditionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState = EditAddition.DataState(
        uuid = "",
        name = "",
        priority = 0,
        price = null,
        isLoading = true,
        isVisible = false,
        fullName = "",
        hasEditFullNameError = false,
        hasEditError = false,
        hasEditNameError = false,
        hasEditPriceError = false

    )
) {

    override fun reduce(action: EditAddition.Action, dataState: EditAddition.DataState) {
        when (action) {
            is EditAddition.Action.OnBackClick -> addEvent { EditAddition.Event.Back }

            EditAddition.Action.OnSaveEditAdditionClick -> updateEditAddition()

            EditAddition.Action.InitAddition -> loadData()

            is EditAddition.Action.OnVisibleClick -> setState {
                copy(
                    isVisible = action.isVisible
                )
            }

            is EditAddition.Action.EditFullNameAddition -> setState {
                copy(
                    fullName = action.fullName
                )
            }

            is EditAddition.Action.EditNameAddition -> setState {
                copy(
                    name = action.name
                )
            }

            is EditAddition.Action.EditPriorityAddition -> setState {
                copy(
                    priority = action.priority.toIntOrNull() ?: 0
                )
            }

            is EditAddition.Action.EditPriceAddition -> {
                action.price.toIntOrNull()?.let { price ->
                    setState {
                        copy(
                            price = price
                        )
                    }
                }
            }
        }
    }

    private fun updateEditAddition() {
        // TODO(Добавить состояние загрузки)

        setState {
            copy(
                hasEditError = false,
                hasEditNameError = false,
                hasEditFullNameError = false,
                hasEditPriceError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                updateAdditionUseCase(
                    updateAddition = state.value.run {
                        UpdateAddition(
                            name = name,
                            priority = priority,
                            fullName = fullName,
                            price = price,
                            isVisible = isVisible
                        )
                    },
                    additionUuid = state.value.uuid
                )
                addEvent {
                    EditAddition.Event.ShowUpdateAdditionSuccess(
                        additionName = state.value.name
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is AdditionNameException -> {
                            copy(hasEditNameError = true)
                        }

                        is AdditionPriceException -> {
                            copy(hasEditPriceError = true)
                        }

                        is AdditionFullNameException -> {
                            copy(hasEditFullNameError = true)
                        }

                        else -> copy(hasEditError = true)
                    }
                }
            }
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val additionUuidNavigation = savedStateHandle.get<String>(ADDITION_UUID) ?: ""
                val addition = getAdditionUseCase(additionUuid = additionUuidNavigation)
                setState {
                    copy(
                        uuid = addition.uuid,
                        name = addition.name,
                        priority = addition.priority,
                        fullName = addition.fullName,
                        price = addition.price,
                        isVisible = addition.isVisible,
                        isLoading = false
                    )
                }
            },
            onError = {
                setState {
                    copy(hasEditError = true)
                }
            }
        )
    }
}
