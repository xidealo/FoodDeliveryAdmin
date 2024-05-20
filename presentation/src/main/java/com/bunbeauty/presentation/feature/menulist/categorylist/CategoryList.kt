package com.bunbeauty.presentation.feature.menulist.categorylist

import com.bunbeauty.domain.model.Category
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CategoryList {
    data class DataState(
        val selectableCategoryList: List<SelectableCategory>,
        val hasError: Boolean,
    ) : BaseDataState {

        data class SelectableCategory(
            val category: Category,
            val selected: Boolean
        )
    }

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnSaveClick : Action
        data object OnBackClick : Action
        data class OnCategoryClick(val uuid: String, val selected: Boolean) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data object Save : Event
    }
}