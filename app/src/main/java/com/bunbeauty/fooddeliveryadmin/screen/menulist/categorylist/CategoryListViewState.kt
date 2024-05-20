package com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CategoryListViewState(
    val selectableCategoryList: ImmutableList<CategoryItem>
) : BaseViewState {
    @Immutable
    data class CategoryItem(
        val uuid: String,
        val name: String,
        val selected: Boolean
    )
}
