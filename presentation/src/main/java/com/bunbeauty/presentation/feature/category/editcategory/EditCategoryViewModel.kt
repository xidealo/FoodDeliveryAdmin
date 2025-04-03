package com.bunbeauty.presentation.feature.category.editcategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.CategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.EditCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryUseCase
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.presentation.extension.launchSafe
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
            name = "",
            isLoading = false,
            state = EditCategoryState.DataState.State.SUCCESS,
            hasEditNameError = false
        )
    ) {
    override fun reduce(action: EditCategoryState.Action, dataState: EditCategoryState.DataState) {
        when (action) {
            is EditCategoryState.Action.EditNameCategory -> editNameCategory(action.nameEditCategory)

            EditCategoryState.Action.Init -> loadData()
            EditCategoryState.Action.OnBackClicked -> onBackClicked()
            EditCategoryState.Action.OnSaveEditCategoryClick -> saveEditCategory()
        }
    }

    private fun editNameCategory(nameEditCategory: String) {
        setState {
            copy(name = nameEditCategory)
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
                        name = category.name
                    )
                }
            },
            onError = {
                // No errors
            }
        )
    }

    private fun saveEditCategory() {
        viewModelScope.launchSafe(
            block = {
                saveEditCategoryUseCase(
                    categoryUuid = state.value.uuid,
                    updateCategory = state.value.run {
                        UpdateCategory(
                            name = name.trim(),
                            priority = null
                        )
                    }
                )
                setState {
                    copy(isLoading = false)
                }

                sendEvent {
                    EditCategoryState.Event.ShowUpdateCategorySuccess(
                        categoryName = state.value.name

                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is CategoryNameException -> {
                            copy(
                                hasEditNameError = true,
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
