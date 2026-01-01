package com.bunbeauty.presentation.feature.login

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Login {
    data class DataState(
        val state: State,
        val username: String,
        val password: String
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {

        data object LoginClick : Action
        data class ChangeLogin(val login: String) : Action
        data class ChangePassword(val password: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object OpenOrderListEvent : Event

        data object ShowWrongCredentialError : Event

        data object ShowConnectionError : Event

    }
}