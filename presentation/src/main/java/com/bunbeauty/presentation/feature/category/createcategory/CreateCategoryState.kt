package com.bunbeauty.presentation.feature.category.createcategory

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CreateCategoryState {
    data class DataState(
        val state: State,
        val isLoading: Boolean,
        val nameField: TextFieldData,
        val hasCreateNameError: Boolean,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object OnBackClicked : Action
        data object OnErrorStateClicked : Action
        data object OnSaveCreateCategoryClick : Action
        data class CreateNameCategoryChanged(val nameCategory: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data class ShowUpdateCategorySuccess(val categoryName: String) : Event
    }
}
