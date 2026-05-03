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
                                    CategoryListViewState.CategoriesViewItem(
                                        uuid = category.uuid,
                                        name = category.name,
                                        priority = category.priority,
                                    )
                                }.toPersistentList(),
                    )

                CategoryListState.DataState.State.DRAG_DROP_SUCCESS ->
                    CategoryListViewState.State.SuccessDragDrop(
                        categoryList = categoryList.toPersistentList(),
                    )
            },
        isRefreshing = isRefreshing,
        categoryList = categoryList.toPersistentList(),
    )
