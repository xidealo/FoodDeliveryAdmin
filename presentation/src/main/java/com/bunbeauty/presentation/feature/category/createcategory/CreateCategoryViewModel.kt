package com.bunbeauty.presentation.feature.category.createcategory

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.CategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.CreateCategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.CreateCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase
) :
    BaseStateViewModel<CreateCategoryState.DataState, CreateCategoryState.Action, CreateCategoryState.Event>(
        initState = CreateCategoryState.DataState(
            state = CreateCategoryState.DataState.State.SUCCESS,
            isLoading = false,
            nameField = TextFieldData.empty,
            hasCreateNameError = true
        )
    ) {

    override fun reduce(
        action: CreateCategoryState.Action,
        dataState: CreateCategoryState.DataState
    ) {
        when (action) {
            is CreateCategoryState.Action.CreateNameCategory -> setState {
                copy(
                    nameField = nameField.copy(
                        value = action.nameCategory,
                        isError = false
                    )
                )
            }

            CreateCategoryState.Action.OnBackClicked -> onBackClicked()

            CreateCategoryState.Action.OnSaveCreateCategoryClick -> saveCreateCategory()
        }
    }

    private fun onBackClicked() {
        sendEvent {
            CreateCategoryState.Event.GoBackEvent
        }
    }

    private fun saveCreateCategory() {
        setState {
            copy(
                nameField = nameField.copy(isError = false)
            )
        }
        viewModelScope.launchSafe(
            block = {
                createCategoryUseCase(
                    createCategory = state.value.run {
                        CreateCategory(
                            name = nameField.value
                        )
                    }

                )
                setState { copy(isLoading = false) }
                sendEvent {
                    CreateCategoryState.Event.ShowUpdateCategorySuccess(
                        categoryName = state.value.nameField.value
                    )
                }
            },
            onError =
            { throwable ->
                setState {
                    when (throwable) {
                        is CategoryNameException -> {
                            copy(
                                hasCreateNameError = true

                            )
                        }

                        is CreateCategoryNameException -> {
                            copy(
                                hasDuplicateNameError = true,
                                isLoading = false
                            )
                        }

                        else -> copy(isLoading = false)
                    }
                }
            }
        )
    }
}
