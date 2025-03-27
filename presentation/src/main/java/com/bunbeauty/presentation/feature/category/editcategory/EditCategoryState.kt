package com.bunbeauty.presentation.feature.category.editcategory

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface EditCategoryState {
    data class DataState(
        val uuid: String,
        val name: String,
        val state: State,
        val isLoading: Boolean,
        val hasEditNameError: Boolean
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data class EditNameCategory(val nameEditCategory: String) : Action
        data object OnBackClicked : Action
        data object Init : Action
        data object OnSaveEditCategoryClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data class ShowUpdateCategorySuccess(val categoryName: String) : Event
    }
}
