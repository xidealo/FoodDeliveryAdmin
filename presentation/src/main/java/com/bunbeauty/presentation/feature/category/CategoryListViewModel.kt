package com.bunbeauty.presentation.feature.category

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlin.uuid.Uuid

class CategoryListViewModel(
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseStateViewModel<CategoryListState.DataState, CategoryListState.Action, CategoryListState.Event>(
    initState = CategoryListState.DataState(
        state = CategoryListState.DataState.State.LOADING,
        categoryList = listOf(),
        isLoading = true,
        isRefreshing = false
    )
) {
    override fun reduce(
        action: CategoryListState.Action,
        dataState: CategoryListState.DataState
    ) {
        when (action) {
            CategoryListState.Action.Init -> loadData()

            CategoryListState.Action.OnBackClicked -> onBackClicked()

            CategoryListState.Action.OnPriorityEditClicked -> onEditPriorityClicked()

            CategoryListState.Action.OnCreateClicked -> onCreateClicked()

            CategoryListState.Action.OnRefreshData -> refreshCategory()

            is CategoryListState.Action.OnCategoryClick -> categoryClick(action.categoryUuid)
        }
    }

    private fun categoryClick(categoryUuid: String) {
        sendEvent {
            CategoryListState.Event.OnCategoryClick(categoryUuid = categoryUuid)
        }
    }

    private fun refreshCategory() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true
                    )
                }
                setState {
                    copy(
                        categoryList = getCategoryListUseCase(refreshing = true),
                        state = CategoryListState.DataState.State.SUCCESS,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = CategoryListState.DataState.State.ERROR
                    )
                }
            }
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        categoryList = getCategoryListUseCase(refreshing = false),
                        state = CategoryListState.DataState.State.SUCCESS,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }, onError = {
                setState {
                    copy(
                        state = CategoryListState.DataState.State.ERROR
                    )
                }
            })
    }

    private fun onBackClicked() {
        sendEvent {
            CategoryListState.Event.GoBackEvent
        }
    }

    private fun onEditPriorityClicked() {
        sendEvent {
            CategoryListState.Event.OnEditPriorityCategoryEvent
        }
    }

    private fun onCreateClicked() {
        sendEvent {
            CategoryListState.Event.CreateCategoryEvent
        }
    }
}
