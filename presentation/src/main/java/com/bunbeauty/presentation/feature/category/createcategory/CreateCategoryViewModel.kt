package com.bunbeauty.presentation.feature.category.createcategory

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.CategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.CreateCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.DuplicateCategoryNameException
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase,
) : BaseStateViewModel<CreateCategoryState.DataState, CreateCategoryState.Action, CreateCategoryState.Event>(
        initState =
            CreateCategoryState.DataState(
                state = CreateCategoryState.DataState.State.SUCCESS,
                isLoading = false,
                nameField = TextFieldData.empty,
                CreateCategoryState.DataState.NameStateError.NO_ERROR,
            ),
    ) {
    override fun reduce(
        action: CreateCategoryState.Action,
        dataState: CreateCategoryState.DataState,
    ) {
        when (action) {
            is CreateCategoryState.Action.CreateNameCategoryChanged ->
                createNameCategoryChanged(
                    action.nameCategory,
                )

            CreateCategoryState.Action.OnBackClicked -> onBackClicked()

            CreateCategoryState.Action.OnSaveCreateCategoryClick ->
                saveCreateCategory(
                    categoryName = dataState.nameField.value,
                )

            CreateCategoryState.Action.OnErrorStateClicked -> onErrorState()
        }
    }

    private fun createNameCategoryChanged(nameCategory: String) {
        setState {
            copy(
                nameField =
                    nameField.copy(
                        value = nameCategory,
                        isError = false,
                    ),
            )
        }
    }

    private fun onErrorState() {
        setState {
            copy(
                state = CreateCategoryState.DataState.State.SUCCESS,
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            CreateCategoryState.Event.GoBackEvent
        }
    }

    private fun saveCreateCategory(categoryName: String) {
        setState {
            copy(
                nameField = nameField.copy(isError = false),
            )
        }
        viewModelScope.launchSafe(
            block = {
                createCategoryUseCase(
                    categoryName = categoryName,
                )

                setState { copy(isLoading = false) }
                sendEvent {
                    CreateCategoryState.Event.ShowUpdateCategorySuccess(
                        categoryName = categoryName,
                    )
                }
            },
            onError =
                { throwable ->
                    setState {
                        when (throwable) {
                            is CategoryNameException -> {
                                copy(
                                    nameStateError =
                                        CreateCategoryState
                                            .DataState.NameStateError.EMPTY_NAME,
                                    nameField =
                                        nameField.copy(
                                            isError = true,
                                        ),
                                    isLoading = false,
                                )
                            }

                            is DuplicateCategoryNameException -> {
                                copy(
                                    nameStateError =
                                        CreateCategoryState
                                            .DataState.NameStateError.DUPLICATE_NAME,
                                    nameField =
                                        nameField.copy(
                                            isError = true,
                                        ),
                                    isLoading = false,
                                )
                            }

                            else ->
                                copy(
                                    isLoading = false,
                                    nameStateError =
                                        CreateCategoryState
                                            .DataState.NameStateError.NO_ERROR,
                                )
                        }
                    }
                },
        )
    }
}
