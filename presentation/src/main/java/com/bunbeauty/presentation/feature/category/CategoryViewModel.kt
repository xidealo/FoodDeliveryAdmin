package com.bunbeauty.presentation.feature.category

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CategoryViewModel(
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseStateViewModel<CategoryState.DataState, CategoryState.Action, CategoryState.Event>(
    initState = CategoryState.DataState(
        state = CategoryState.DataState.State.LOADING,
        categoryList = listOf(),
        isLoading = true,
        isRefreshing = false
    )
) {
    override fun reduce(
        action: CategoryState.Action,
        dataState: CategoryState.DataState
    ) {
        when (action) {
            CategoryState.Action.Init -> loadData()

            CategoryState.Action.OnBackClicked -> onBackClicked()

            CategoryState.Action.OnPriorityEditClicked -> onEditPriorityClicked()

            is CategoryState.Action.OnCategoryClick -> sendEvent {
                CategoryState.Event.OnCategoryClick(categoryUuid = action.categoryUuid)
            }

            CategoryState.Action.OnCreateClicked -> onCreateClicked()

            CategoryState.Action.OnRefreshData -> refreshCategory()
        }
    }

    private fun refreshCategory() {
        viewModelScope.launchSafe(block = {
            setState {
                copy(
                    isRefreshing = true
                )
            }
            setState {
                copy(
                    categoryList = getCategoryListUseCase(refreshing = true),
                    state = CategoryState.DataState.State.SUCCESS,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }, onError = {
                setState {
                    copy(
                        state = CategoryState.DataState.State.ERROR
                    )
                }
            })
    }

    private fun loadData() {
        viewModelScope.launchSafe(block = {
            setState {
                copy(
                    categoryList = getCategoryListUseCase(refreshing = false),
                    state = CategoryState.DataState.State.SUCCESS,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }, onError = {
                setState {
                    copy(
                        state = CategoryState.DataState.State.ERROR
                    )
                }
            })
    }

    private fun onBackClicked() {
        sendEvent {
            CategoryState.Event.GoBackEvent
        }
    }

    private fun onEditPriorityClicked() {
        sendEvent {
            CategoryState.Event.GoEditCategoryEvent
        }
    }

    private fun onCreateClicked() {
        sendEvent {
            CategoryState.Event.CreateCategoryEvent
        }
    }
}
