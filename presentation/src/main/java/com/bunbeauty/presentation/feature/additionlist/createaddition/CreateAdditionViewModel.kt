package com.bunbeauty.presentation.feature.additionlist.createaddition

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.feature.additionlist.createaddition.CreateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAddition.*
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.CreateMenuProduct
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAdditionViewModel @Inject constructor(
    private val createAdditionUseCase: CreateAdditionUseCase
) :
    BaseStateViewModel<CreateAddition.DataState, CreateAddition.Action, CreateAddition.Event>(
        initState = DataState(
            uuid = "",
            name = "",
            priority = 0,
            price = null,
            isLoading = true,
            isVisible = false,
            fullName = "",
            hasCreateNameError = false,
            hasCreatePriceError = false
        )
    ) {

    override fun reduce(action: CreateAddition.Action, dataState: CreateAddition.DataState) {
        when (action) {
            is CreateAddition.Action.OnBackClick -> sendEvent { CreateAddition.Event.Back }

            CreateAddition.Action.OnSaveCreateAdditionClick -> saveCreateAddition()

            CreateAddition.Action.CreateAddition -> loadData()

            is CreateAddition.Action.OnVisibleClick -> setState {
                copy(
                    isVisible = action.isVisible
                )
            }

            is CreateAddition.Action.CreateFullNameAddition -> setState {
                copy(
                    fullName = action.fullName
                )
            }

            is CreateAddition.Action.CreateNameAddition -> setState {
                copy(
                    name = action.name
                )
            }

            is CreateAddition.Action.CreatePriorityAddition -> setState {
                copy(
                    priority = action.priority.toIntOrNull() ?: 0
                )
            }

            is CreateAddition.Action.CreatePriceAddition -> setState {
                copy(
                    price = action.price.toIntOrNull()
                )
            }
        }

    }


    private fun saveCreateAddition() {
        setState {
            copy(
                isLoading = true,
                hasCreateNameError = false,
                hasCreatePriceError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                createAdditionUseCase(
                    params = state.value.run {
                        CreateAdditionUseCase.Params(
                            name = name,
                            priority = priority,
                            fullName = fullName,
                            price = price,
                            isVisible = isVisible
                        )
                    }
                )
                sendEvent {
                    CreateAddition.Event.ShowSaveAdditionSuccess()
                }
            },
            onError = { throwable ->
                setState { copy(sendingToServer = false) }
                when (throwable) {
                    is MenuProductNameException -> {
                        setState {
                            copy(hasNameError = true)
                        }
                    }

                    is MenuProductNewPriceException -> {
                        setState {
                            copy(hasCreatePriceError = true)
                        }
                    }

                    else -> {
                        sendEvent {
                            CreateMenuProduct.Event.ShowSomethingWentWrong
                        }
                    }
                }
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
}