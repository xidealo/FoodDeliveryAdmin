package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateaddition.AdditionNameException
import com.bunbeauty.domain.feature.additionlist.UpdateAdditionUseCase
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import com.bunbeauty.presentation.designsystem.compose.element.image.ImageData
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.state.EditAddition
import com.bunbeauty.presentation.feature.image.EditImageFieldData
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.image.ProductImage
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.ImageFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val ADDITION_UUID = "additionUuid"

class EditAdditionViewModel(
    private val getAdditionUseCase: GetAdditionUseCase,
    private val updateAdditionUseCase: UpdateAdditionUseCase,
    private val additionUuid: String
) : BaseStateViewModel<EditAddition.DataState, EditAddition.Action, EditAddition.Event>(
    initState =
        EditAddition.DataState(
            state = EditAddition.DataState.State.LOADING,
            uuid = "",
            name = "",
            price = "",
            isLoading = true,
            isVisible = false,
            fullName = "",
            hasEditNameError = false,
            tag = "",
            imageFieldData =
                ImageFieldUi(
                    value = null,
                    isError = false,
                    isSelected = false,
                ),
        ),
) {

    init {
        loadData()
    }

    override fun reduce(
        action: EditAddition.Action,
        dataState: EditAddition.DataState,
    ) {
        when (action) {
            is EditAddition.Action.OnBackClick -> backClick()

            EditAddition.Action.OnSaveEditAdditionClick -> updateEditAddition()

            EditAddition.Action.InitAddition -> loadData()

            is EditAddition.Action.OnVisibleClick -> editOnVisible(isVisible = action.isVisible)

            is EditAddition.Action.EditFullNameAddition ->
                editFullNameAddition(
                    fullName = action.fullName,
                )

            is EditAddition.Action.EditNameAddition -> editNameAddition(name = action.name)

            is EditAddition.Action.EditPriceAddition -> editPriceAddition(price = action.price)

            is EditAddition.Action.EditTagAddition -> editTagAddition(tag = action.tag)

            is EditAddition.Action.SetImage -> setImage(croppedImageUri = action.croppedImageUri)
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    val addition = getAdditionUseCase(additionUuid = additionUuid)
                    copy(
                        uuid = addition.uuid,
                        name = name,
                        fullName = addition.fullName.orEmpty(),
                        price = addition.price?.toString().orEmpty(),
                        isVisible = addition.isVisible,
                        tag = addition.tag.orEmpty(),
                        isLoading = false,
                        state = EditAddition.DataState.State.SUCCESS,
                        imageFieldData =
                            ImageFieldUi(
                                value = ImageData.HttpUrl(
                                    url = addition.photoLink,
                                ),
                                isError = false,
                                isSelected = false,
                            ),
                    )
                }
            },
            onError = {
                // No errors
            },
        )
    }

    private fun editOnVisible(isVisible: Boolean) {
        setState {
            copy(
                isVisible = isVisible,
            )
        }
    }

    private fun backClick() {
        sendEvent { EditAddition.Event.Back }
    }

    private fun editFullNameAddition(fullName: String) {
        setState {
            copy(
                fullName = fullName,
            )
        }
    }

    private fun editNameAddition(name: String) {
        setState {
            copy(
                name = name,
            )
        }
    }

    private fun editPriceAddition(price: String) {
        setState {
            copy(
                price = price,
            )
        }
    }

    private fun editTagAddition(tag: String) {
        setState {
            copy(
                tag = tag,
            )
        }
    }

    fun setImage(croppedImageUri: String) {
        setState {
            copy(
//                imageFieldData =
//                    imageFieldData.copy(
//                        value =
//                            imageFieldData.value?.copy(
//                                newImageUri = croppedImageUri,
//                            ),
//                        isError = false,
//                    ),
            )
        }
    }

    private fun updateEditAddition() {
        setState {
            copy(
                isLoading = true,
                hasEditNameError = false,
            )
        }
        viewModelScope.launchSafe(
            block = {
                updateAdditionUseCase(
                    updateAddition =
                        state.value.run {
                            UpdateAddition(
                                name = name.trim(),
                                fullName = fullName.takeIf { fullName.isNotBlank() }?.trim(),
                                price = price.toIntOrNull(),
                                isVisible = isVisible,
                                tag = tag.trim(),
//                                photoLink = imageFieldData.value?.photoLink,
//                                newImageUri = imageFieldData.value?.newImageUri,
                            )
                        },
                    additionUuid = state.value.uuid,
                )
                setState {
                    copy(isLoading = false)
                }
                sendEvent {
                    EditAddition.Event.ShowUpdateAdditionSuccess(
                        additionName = state.value.name,
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is AdditionNameException -> {
                            copy(hasEditNameError = true, isLoading = false)
                        }

                        else -> copy(isLoading = false)
                    }
                }
            },
        )
    }
}
