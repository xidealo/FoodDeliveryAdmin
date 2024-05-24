package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import javax.inject.Inject
private const val ADDITION_UUID = "additionUuid"
class EditAdditionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase,
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState = EditAddition.DataState(
        uuid = "",
        name = "",
        priority = 0,
        prise = null,
        isLoading = false,
        isVisible = false,
        fullName = "",
        hasEditFullNameError = false,
        hasEditError = true,
    )
) {
    private val additionUuidNavigation: String? = savedStateHandle.get<String>(
        ADDITION_UUID
    )

    override fun reduce(action: EditAddition.Action, dataState: EditAddition.DataState) {
        when (action) {
            EditAddition.Action.OnBackClick -> addEvent { EditAddition.Event.Back }

            EditAddition.Action.InitAddition -> loadData(dataState.uuid)

            is EditAddition.Action.OnVisibleClick -> setState {
                copy(
                    isVisible = action.isVisible
                )
            }

            EditAddition.Action.SaveEditAdditionClick -> updateEditAddition()
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

            is EditAddition.Action.EditPriseAddition -> setState {
                copy(
                    prise = action.prise.toIntOrNull()
                )
            }
        }
    }

    private fun updateEditAddition() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isLoading = isLoading,
                        hasEditError = hasEditError,
                        hasEditFullNameError = hasEditFullNameError
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
            onError = {
                setState {
                    copy(hasEditError = true)
                }
            }
        )
    }

    private fun loadData(additionUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val addition = getAdditionUseCase(additionUuid)
                setState {
                    copy(
                        uuid = additionUuidNavigation.toString(),
                        name = addition.name,
                        priority = addition.priority?: 0,
                        fullName = addition.fullName,
                        prise = addition.price,
                        isVisible = addition.isVisible,
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




