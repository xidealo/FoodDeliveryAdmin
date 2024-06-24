package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

private const val ADDITION_UUID = "additionUuid"

@HiltViewModel
class EditAdditionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase,
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState = EditAddition.DataState(
        uuid = "",
        name = "",
        priority = 0,
        prise = "",
        isLoading = false,
        isVisible = false,
        fullName = "",
        hasEditFullNameError = false,
        hasEditError = false,
        hasEditNameError = false,
        hasEditPriseError = false

    )
) {


    override fun reduce(action: EditAddition.Action, dataState: EditAddition.DataState) {
        when (action) {
            EditAddition.Action.OnBackClick -> addEvent { EditAddition.Event.Back }

            EditAddition.Action.InitAddition -> loadData()

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
                    prise = action.prise
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
                        isVisible = !isVisible,
                        updateAddition = UpdateAddition(
                            name = name.trim(),
                            priority = priority,
                            fullName = fullName,
                            price = prise.toIntOrNull(),
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

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val additionUuidNavigation = savedStateHandle.get<String>(ADDITION_UUID) ?: ""
                Log.d("my_tag", "loadData: $additionUuidNavigation")
                val addition = getAdditionUseCase(additionUuid = additionUuidNavigation)
                setState {
                    copy(
                        uuid = addition.uuid,
                        name = addition.name,
                        priority = addition.priority,
                        fullName = addition.fullName,
                        prise = addition.price.toString(),
                        isVisible = addition.isVisible,
                    )
                }
                Log.d("my_prise", "loadData: ${addition.price}")
            },
            onError = {
                setState {
                    copy(hasEditError = true)
                }
            }
        )
    }
}




