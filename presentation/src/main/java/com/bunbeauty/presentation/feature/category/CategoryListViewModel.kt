package com.bunbeauty.presentation.feature.category

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.category.SaveCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CategoryListViewModel(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val saveCategoryListUseCase: SaveCategoryListUseCase
) : BaseStateViewModel<CategoryListState.DataState, CategoryListState.Action, CategoryListState.Event>(
    initState = CategoryListState.DataState(
        state = CategoryListState.DataState.State.LOADING,
        categoryList = listOf(),
        isLoading = true,
        isRefreshing = false,
        isEditPriority = false
    )
) {
    override fun reduce(
        action: CategoryListState.Action,
        dataState: CategoryListState.DataState
    ) {
        when (action) {
            CategoryListState.Action.Init -> loadData()

            CategoryListState.Action.OnBackClicked -> onBackClicked()

            CategoryListState.Action.OnCancelClicked -> cancelEditPriority()

            CategoryListState.Action.OnPriorityEditClicked -> onEditPriorityClicked()

            CategoryListState.Action.OnCreateClicked -> onCreateClicked()

            CategoryListState.Action.OnRefreshData -> refreshCategory()

            is CategoryListState.Action.OnCategoryClick -> categoryClick(action.categoryUuid)

            is CategoryListState.Action.OnSaveEditPriorityCategoryClick -> {
                val updatedList = action.updatedList.withUpdatedPriorities()
                saveCategoryDrop(updatedList)
            }

            is CategoryListState.Action.SwapItem -> {
                setState {
                    copy(
                        categoryList = dataState.categoryList.toMutableList()
                            .apply {
                                swap(action.fromIndex, action.toIndex)
                            }
                    )
                }
            }
        }
    }

    private fun categoryClick(categoryUuid: String) {
        sendEvent {
            CategoryListState.Event.OnCategoryClick(categoryUuid = categoryUuid)
        }
    }

    private fun <T> MutableList<T>.swap(fromIndex: Int, toIndex: Int) {
        val tmp = this[fromIndex]
        this[fromIndex] = this[toIndex]
        this[toIndex] = tmp
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

    private fun saveCategoryDrop(category: List<Category>) {
        viewModelScope.launchSafe(
            block = {
                saveCategoryListUseCase(
                    categoryList = category
                )
                setState {
                    copy(
                        isLoading = false,
                        isEditPriority = false,
                        state = CategoryListState.DataState.State.SUCCESS
                    )
                }
                sendEvent { CategoryListState.Event.ShowUpdateCategoryListSuccess }
                loadData()
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

    private fun cancelEditPriority() {
        setState {
            copy(
                isEditPriority = false
            )
        }
    }

    private fun onBackClicked() {
        sendEvent {
            CategoryListState.Event.GoBackEvent
        }
    }

    private fun onEditPriorityClicked() {
        setState {
            copy(
                isEditPriority = true
            )
        }
    }

    private fun onCreateClicked() {
        sendEvent {
            CategoryListState.Event.CreateCategoryEvent
        }
    }

    private fun List<Category>.withUpdatedPriorities(): List<Category> {
        return mapIndexed { index, category ->
            category.copy(priority = index + 1)
        }
    }
}
