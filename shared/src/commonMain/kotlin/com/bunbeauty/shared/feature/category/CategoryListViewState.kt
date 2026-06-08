package com.bunbeauty.shared.feature.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class CategoryListViewState(
    val state: State,
    val isRefreshing: Boolean,
    val categoryList: ImmutableList<Category>,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val categoryList: ImmutableList<CategoriesViewItem>,
            val isSearchEnabled: Boolean,
            val searchQuery: String,
            val searchResultList: ImmutableList<CategoriesViewItem>?,
        ) : State

        data class SuccessDragDrop(
            val categoryList: ImmutableList<Category>,
        ) : State
    }

    @Immutable
    data class CategoriesViewItem(
        val uuid: String,
        val name: String,
        val priority: Int,
    )
}

@Composable
internal fun CategoryListState.DataState.toViewState(): CategoryListViewState =
    CategoryListViewState(
        state =
            when (state) {
                CategoryListState.DataState.State.LOADING -> CategoryListViewState.State.Loading
                CategoryListState.DataState.State.ERROR -> CategoryListViewState.State.Error
                CategoryListState.DataState.State.SUCCESS ->
                    CategoryListViewState.State.Success(
                        categoryList =
                            categoryList
                                .map { category ->
                                    category.toItem()
                                }.toPersistentList(),
                        isSearchEnabled = isSearchEnabled,
                        searchQuery = searchQuery,
                        searchResultList = getSearchResultList(),
                    )

                CategoryListState.DataState.State.DRAG_DROP_SUCCESS ->
                    CategoryListViewState.State.SuccessDragDrop(
                        categoryList = categoryList.toPersistentList(),
                    )
            },
        isRefreshing = isRefreshing,
        categoryList = categoryList.toPersistentList(),
    )

private fun CategoryListState.DataState.getSearchResultList(): ImmutableList<CategoryListViewState.CategoriesViewItem>? {
    val normalizedSearchQuery = searchQuery.trim()
    if (!isSearchEnabled || normalizedSearchQuery.isEmpty()) {
        return null
    }

    return categoryList
        .filter { category ->
            category.name.contains(normalizedSearchQuery, ignoreCase = true)
        }.map { category ->
            category.toItem()
        }.toPersistentList()
}

private fun Category.toItem(): CategoryListViewState.CategoriesViewItem =
    CategoryListViewState.CategoriesViewItem(
        uuid = uuid,
        name = name,
        priority = priority,
    )
