package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateaddition.AdditionFullNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriseException
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
            is EditAddition.Action.OnBackClick -> addEvent { EditAddition.Event.Back }

//            is EditAddition.Action.OnSaveEditAdditionClick ->
//                addEvent {
//                    EditAddition.Event.ShowUpdateAdditionSuccess(
//                        additionName = dataState.name
//                    )
//                }

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

            is EditAddition.Action.EditPriseAddition -> setState {
                copy(
                    prise = action.prise
                )
            }
        }
    }

    private fun updateEditAddition() {
        setState {
            copy(
                hasEditError = false,
                hasEditNameError = false,
                hasEditFullNameError = false,
                hasEditPriseError = false,
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
                            prise = prise.toIntOrNull() ?: 0,
                            isVisible = isVisible,
                        )
                    },
                    additionUuid = state.value.uuid
                )

            },
            onError =
            { throwable ->
                setState {
                    when (throwable) {
                        is AdditionNameException -> {
                            copy(hasEditError = true)
                        }

                        is AdditionPriseException -> {
                            copy(hasEditPriseError = true)
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




