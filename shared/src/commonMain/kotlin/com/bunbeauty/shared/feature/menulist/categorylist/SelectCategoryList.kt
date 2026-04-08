package com.bunbeauty.shared.feature.menulist.categorylist

import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface SelectCategoryList {
    data class DataState(
        val selectableCategoryList: List<SelectableCategory>,
        val hasError: Boolean,
    ) : BaseDataState {
        val selectedCategoryList: List<SelectableCategory>
            get() {
                return selectableCategoryList.filter { selectableCategory ->
                    selectableCategory.selected
                }
            }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val selectedCategoryList: List<String>,
        ) : Action

        data object OnSaveClick : Action

        data object OnBackClick : Action

        data class OnCategoryClick(
            val uuid: String,
            val selected: Boolean,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class Save(
            val selectedCategoryUuidList: List<String>,
        ) : Event
    }
}
