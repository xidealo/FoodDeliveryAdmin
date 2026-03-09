package com.bunbeauty.presentation.feature.menulist.categorylist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class SelectCategoryListViewState(
    val selectableCategoryList: ImmutableList<SelectCategoryItem>,
) : BaseViewState {
    @Immutable
    data class SelectCategoryItem(
        val uuid: String,
        val name: String,
        val selected: Boolean,
    )
}

@Composable
internal fun SelectCategoryList.DataState.toViewState(): SelectCategoryListViewState =
    SelectCategoryListViewState(
        selectableCategoryList =
            selectableCategoryList
                .map { selectableCategory ->
                    SelectCategoryListViewState.SelectCategoryItem(
                        uuid = selectableCategory.category.uuid,
                        name = selectableCategory.category.name,
                        selected = selectableCategory.selected,
                    )
                }.toPersistentList(),
    )
