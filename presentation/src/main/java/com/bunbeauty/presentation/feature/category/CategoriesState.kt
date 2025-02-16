package com.bunbeauty.presentation.feature.category

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CategoriesState {
    data class DataState(val state: State) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object OnBackClicked : Action
        data object Init : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
    }
}