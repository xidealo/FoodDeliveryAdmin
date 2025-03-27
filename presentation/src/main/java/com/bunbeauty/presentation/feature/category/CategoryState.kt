package com.bunbeauty.presentation.feature.category

import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CategoryState {
    data class DataState(
        val state: State,
        val isLoading: Boolean,
        val categoryList: List<Category>,
        val isRefreshing: Boolean
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object OnRefreshData : Action
        data object OnBackClicked : Action
        data object OnEditClicked : Action
        data object OnCreateClicked : Action
        data object Init : Action
        data class OnCategoryClick(val categoryUuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data object GoEditCategoryEvent : Event
        data object CreateCategoryEvent : Event
        data class OnCategoryClick(val categoryUuid: String) : Event
    }
}
