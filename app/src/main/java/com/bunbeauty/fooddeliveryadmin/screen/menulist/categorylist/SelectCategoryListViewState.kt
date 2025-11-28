package com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SelectCategoryListViewState(
    val selectableCategoryList: ImmutableList<SelectCategoryItem>
) : BaseViewState {
    @Immutable
    data class SelectCategoryItem(
        val uuid: String,
        val name: String,
        val selected: Boolean
    )
}
