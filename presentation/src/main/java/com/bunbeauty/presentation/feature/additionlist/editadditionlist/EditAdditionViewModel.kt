package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.exception.updateaddition.AdditionPriorityException
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.domain.usecase.UpdateAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.image.EditImageFieldData
import com.bunbeauty.presentation.feature.image.ProductImage
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val ADDITION_UUID = "additionUuid"

class EditAdditionViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState = EditAddition.DataState(
        uuid = "",
        name = "",
        priority = "",
        price = "",
        isLoading = true,
        isVisible = false,
        fullName = "",
        hasEditNameError = false,
        hasEditPriorityError = false,
        tag = "",
        imageFieldData = EditImageFieldData(
            value = null,
            isError = false
        )
    )
) {

    override fun reduce(action: EditAddition.Action, dataState: EditAddition.DataState) {
        when (action) {
            is EditAddition.Action.OnBackClick -> sendEvent { EditAddition.Event.Back }

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
                    priority = action.priority
                )
            }

            is EditAddition.Action.EditPriceAddition -> setState {
                copy(
                    price = action.price
                )
            }

            is EditAddition.Action.EditTagAddition -> setState {
                copy(
                    tag = action.tag
                )
            }

            is EditAddition.Action.SetImage -> {
                setState {
                    copy(
                        imageFieldData = imageFieldData.copy(
                            value = imageFieldData.value?.copy(
                                newImageUri = action.croppedImageUri
                            ),
                            isError = false
                        )
                    )
                }
            }
        }
    }

    private fun updateEditAddition() {
        setState {
            copy(
                isLoading = true,
                hasEditNameError = false,
                hasEditPriorityError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                updateAdditionUseCase(
                    updateAddition = state.value.run {
                        UpdateAddition(
                            name = name.trim(),
                            priority = priority.toIntOrNull(),
                            fullName = fullName.takeIf { fullName.isNotBlank() }?.trim(),
                            price = price.toIntOrNull(),
                            isVisible = isVisible,
                            tag = tag,
                            photoLink = imageFieldData.value?.photoLink,
                            newImageUri = imageFieldData.value?.newImageUri
                        )
                    },
                    additionUuid = state.value.uuid
                )
                setState {
                    copy(isLoading = false)
                }
                sendEvent {
                    EditAddition.Event.ShowUpdateAdditionSuccess(
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

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    val additionUuidNavigation =
                        savedStateHandle.get<String>(ADDITION_UUID).orEmpty()
                    val addition = getAdditionUseCase(additionUuid = additionUuidNavigation)
                    copy(
                        uuid = addition.uuid,
                        name = addition.name,
                        priority = addition.priority.toString(),
                        fullName = addition.fullName.orEmpty(),
                        price = addition.price?.toString().orEmpty(),
                        isVisible = addition.isVisible,
                        tag = addition.tag.orEmpty(),
                        isLoading = false,
                        imageFieldData = EditImageFieldData(
                            value = ProductImage(
                                photoLink = addition.photoLink,
                                newImageUri = null
                            ),
                            isError = false
                        )
                    )
                }
            },
            onError = {
                // No errors
            }
        )
    }
}
