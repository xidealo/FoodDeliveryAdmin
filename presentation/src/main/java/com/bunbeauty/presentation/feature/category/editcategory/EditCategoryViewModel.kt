package com.bunbeauty.presentation.feature.category.editcategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.CategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.DuplicateCategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.EditCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val CATEGORY_UUID = "categoryUuid"

class EditCategoryViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val saveEditCategoryUseCase: EditCategoryUseCase
) :
    BaseStateViewModel<EditCategoryState.DataState, EditCategoryState.Action, EditCategoryState.Event>(
        initState = EditCategoryState.DataState(
            uuid = "",
            name = TextFieldData.empty,
            isLoading = false,
            state = EditCategoryState.DataState.State.SUCCESS,
            nameStateError = EditCategoryState.DataState.NameStateError.NO_ERROR
        )
    ) {

    override fun reduce(action: EditCategoryState.Action, dataState: EditCategoryState.DataState) {
        when (action) {
            is EditCategoryState.Action.EditNameCategory -> editNameCategory(action.nameEditCategory)

            EditCategoryState.Action.Init -> loadData()
            EditCategoryState.Action.OnBackClicked -> onBackClicked()
            EditCategoryState.Action.OnSaveEditCategoryClick -> saveEditCategory(
                dataState.uuid,
                dataState.name.value
            )
        }
    }

    private fun editNameCategory(nameEditCategory: String) {
        setState {
            copy(
                name = name.copy(
                    value = nameEditCategory,
                    isError = false
                )
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            EditCategoryState.Event.GoBackEvent
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val categoryUuidNavigation =
                    savedStateHandle.get<String>(CATEGORY_UUID).orEmpty()
                val category = getCategoryUseCase(categoryUuid = categoryUuidNavigation)
                setState {
                    copy(
                        uuid = category.uuid,
                        name = name.copy(
                            value = category.name,
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

    private fun saveEditCategory(
        categoryUuid: String,
        categoryName: String
    ) {
        viewModelScope.launchSafe(
            block = {
                saveEditCategoryUseCase(
                    categoryUuid = categoryUuid,
                    updateCategory = state.value.run {
                        UpdateCategory(
                            name = categoryName.trim(),
                            priority = null
                        )
                    }
                )
                setState {
                    copy(isLoading = false)
                }

                sendEvent {
                    EditCategoryState.Event.ShowUpdateCategorySuccess(
                        categoryName = categoryName

                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is CategoryNameException -> {
                            copy(
                                nameStateError = EditCategoryState
                                    .DataState.NameStateError.EMPTY_NAME,
                                name = name.copy(
                                    isError = true
                                ),
                                isLoading = false
                            )
                        }

                        is DuplicateCategoryNameException -> {
                            copy(
                                nameStateError = EditCategoryState
                                    .DataState.NameStateError.DUPLICATE_NAME,
                                name = name.copy(
                                    isError = true
                                ),
                                isLoading = false
                            )
                        }

                        else -> copy(
                            isLoading = false,
                            nameStateError = EditCategoryState
                                .DataState.NameStateError.NO_ERROR
                        )
                    }
                }
            }
        )
    }
}
