package com.bunbeauty.presentation.feature.additionlist.createaddition

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.feature.additionlist.CreateAdditionUseCase
import com.bunbeauty.domain.model.addition.CreateAdditionModel
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAddition
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionViewModel(
    private val createAdditionUseCase: CreateAdditionUseCase
) :
    BaseStateViewModel<CreateAddition.DataState, CreateAddition.Action, CreateAddition.Event>(
        initState = CreateAddition.DataState(
            uuid = "",
            name = "",
            priority = "",
            price = "",
            isLoading = false,
            isVisible = false,
            fullName = "",
            hasEditNameError = false,
            hasEditPriorityError = false,
            tag = "",
            //  imageFieldData = EditImageFieldData(
            //      value = null,
            //      isError = false
            //  )
        )
    ) {
    override fun reduce(
        action: CreateAddition.Action,
        dataState: CreateAddition.DataState
    ) {
        when (action) {
            is CreateAddition.Action.EditFullNameAddition -> editFullNameAddition(
                fullName = action.fullName
            )

            is CreateAddition.Action.EditNameAddition -> editNameAddition(name = action.name)
            is CreateAddition.Action.EditPriceAddition -> editPriceAddition(price = action.price)
            is CreateAddition.Action.EditPriorityAddition -> editPriorityAddition(
                priority = action.priority
            )

            is CreateAddition.Action.EditTagAddition -> editTagAddition(tag = action.tag)
            CreateAddition.Action.OnBackClick -> backClick()
            CreateAddition.Action.OnSaveCreateAdditionClick -> createAddition()
            is CreateAddition.Action.OnVisibleClick -> editOnVisible(isVisible = action.isVisible)
            is CreateAddition.Action.SetImage -> setImage(croppedImageUri = action.croppedImageUri)
        }
    }


    private fun editOnVisible(isVisible: Boolean) {
        setState {
            copy(
                isVisible = isVisible
            )
        }
    }

    private fun backClick() {
        sendEvent { CreateAddition.Event.Back }
    }

    private fun editFullNameAddition(fullName: String) {
        setState {
            copy(
                fullName = fullName
            )
        }
    }

    private fun editNameAddition(name: String) {
        setState {
            copy(
                name = name
            )
        }
    }

    private fun editPriorityAddition(priority: String) {
        setState {
            copy(
                priority = priority
            )
        }
    }

    private fun editPriceAddition(price: String) {
        setState {
            copy(
                price = price
            )
        }
    }

    private fun editTagAddition(tag: String) {
        setState {
            copy(
                tag = tag
            )
        }
    }

    private fun setImage(croppedImageUri: String) {
//        setState {
//            copy(
//                imageFieldData = imageFieldData.copy(
//                    value = imageFieldData.value?.copy(
//                        newImageUri = croppedImageUri
//                    ),
//                    isError = false
//                )
//            )
//        }
    }

    fun createAddition() {
        setState {
            copy(
                isLoading = true,
                hasEditNameError = false,
                hasEditPriorityError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                createAdditionUseCase(
                    createAdditionModel = state.value.run {
                        CreateAdditionModel(
                            name = name.trim(),
                            priority = priority.toIntOrNull(),
                            fullName = fullName.takeIf { fullName.isNotBlank() }?.trim(),
                            price = price.toIntOrNull(),
                            isVisible = isVisible,
                            tag = tag,
                           //photoLink = imageFieldData.value?.photoLink,
                           //newImageUri = imageFieldData.value?.newImageUri
                        )
                    },
                )
                setState {
                    copy(isLoading = false)
                }
                sendEvent {
                    CreateAddition.Event.ShowCreatedAdditionSuccess(
                        additionName = state.value.name
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is AdditionNameException -> {
                            copy(hasEditNameError = true, isLoading = false)
                        }

                        is AdditionPriorityException -> {
                            copy(hasEditPriorityError = true, isLoading = false)
                        }

                        else -> copy(isLoading = false)
                    }
                }
            }
        )
    }
}