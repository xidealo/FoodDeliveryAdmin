package com.bunbeauty.presentation.feature.additionlist.createaddition

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.createaddition.CreateAdditionUseCase
import com.bunbeauty.domain.feature.additionlist.exception.AdditionNameException
import com.bunbeauty.domain.feature.additionlist.exception.AdditionPriceException
import com.bunbeauty.domain.feature.additionlist.exception.AdditionPriorityException
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAdditionViewModel @Inject constructor(
    private val createAdditionUseCase: CreateAdditionUseCase
) :
    BaseStateViewModel<CreateAddition.DataState, CreateAddition.Action, CreateAddition.Event>(
        initState = CreateAddition.DataState(
            name = TextFieldData.empty,
            priority = TextFieldData.empty,
            price = TextFieldData.empty,
            isLoading = false,
            isVisible = true,
            fullName = "",
            hasCreateNameError = false,
            hasCreatePriceError = false,
            photoLink = ""
        )
    ) {

    override fun reduce(action: CreateAddition.Action, dataState: CreateAddition.DataState) {
        when (action) {
            is CreateAddition.Action.OnBackClick -> sendEvent { CreateAddition.Event.Back }

            CreateAddition.Action.OnSaveCreateAdditionClick -> saveCreateAddition()

            CreateAddition.Action.CreateAddition -> loadData()

            is CreateAddition.Action.OnVisibleClick -> setState {
                copy(
                    isVisible = !isVisible
                )
            }

            is CreateAddition.Action.CreateFullNameAddition -> setState {
                copy(
                    fullName = action.fullName
                )
            }

            is CreateAddition.Action.CreateNameAddition -> setState {
                copy(
                    name = name.copy(
                        value = action.name,
                        isError = false
                    )
                )
            }

            is CreateAddition.Action.CreatePriorityAddition -> setState {
                copy(
                    priority = priority.copy(
                        value = action.priority,
                        isError = false
                    )
                )
            }

            is CreateAddition.Action.CreatePriceAddition -> setState {
                copy(
                    price = price.copy(
                        value = action.price,
                        isError = false
                    )
                )
            }
        }
    }

    private fun saveCreateAddition() {
        setState {
            copy(
                name = name.copy(isError = false),
                priority = priority.copy(isError = false),
                price = price.copy(isError = false)
            )
        }
        viewModelScope.launchSafe(
            block = {
                createAdditionUseCase(
                    params = state.value.run {
                        CreateAdditionUseCase.Params(
                            name = name.value.trim(),
                            priority = priority.value.trim(),
                            fullName = fullName.trim(),
                            price = price.value.trim(),
                            isVisible = isVisible,
                            photoLink = photoLink
                        )
                    }
                )
                sendEvent { state ->
                    CreateAddition.Event.ShowSaveAdditionSuccess(
                        additionName = state.name.value
                    )
                }
            },
            onError = { throwable ->
                handleCreateMenuProductError(throwable = throwable)
            }
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
            },
            onError = {
                // No errors
            }
        )
    }

    private fun handleCreateMenuProductError(throwable: Throwable) {
        when (throwable) {
            is AdditionNameException -> {
                setState {
                    copy(
                        name = name.copy(
                            isError = true
                        )
                    )
                }
            }

            is AdditionPriceException -> {
                setState {
                    copy(
                        price = price.copy(
                            isError = true
                        )
                    )
                }
            }

            is AdditionPriorityException -> {
                setState {
                    copy(
                        priority = priority.copy(
                            isError = true
                        )
                    )
                }
            }

            else -> {
                sendEvent {
                    CreateAddition.Event.ShowSomethingWentWrong
                }
            }
        }
    }
}
