package com.bunbeauty.shared.feature.login

import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseEvent
import com.bunbeauty.shared.viewmodel.base.BaseViewDataState

interface Login {
    data class DataState(
        val state: State,
        val username: String,
        val password: String,
        val isPasswordVisible: Boolean,
        val startLoginLoading: Boolean,
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            SUCCESS,
        }
    }

    sealed interface Action : BaseAction {
        data object LoginClick : Action

        data class ChangeUsername(
            val username: String,
        ) : Action

        data class ChangePassword(
            val password: String,
        ) : Action

        data object ChangeVisiblePassword : Action
    }

    sealed interface Event : BaseEvent {
        data object OpenOrderListEvent : Event

        data object ShowWrongCredentialError : Event

        data object ShowWrongLoginError : Event

        data object ShowConnectionError : Event
    }
}
