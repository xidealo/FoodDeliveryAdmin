package com.bunbeauty.fooddeliveryadmin.screen.category

import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CategoryListViewState(
    val state: State,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val isEditPriority: Boolean
) : BaseViewState {

    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val categoryList: ImmutableList<CategoriesViewItem>
        ) : State
        data class SuccessDragDrop(
            val categoryList: List<Category>
        ) : State
    }

    @Immutable
    data class CategoriesViewItem(
        val uuid: String,
        val name: String,
        val priority: Int
    )
}
