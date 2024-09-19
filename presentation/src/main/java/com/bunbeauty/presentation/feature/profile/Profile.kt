package com.bunbeauty.presentation.feature.profile

import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface Profile {

    data class DataState(
        val state: State,
        val user: User?,
        val acceptOrders: Boolean,
        val showAcceptOrdersConfirmation: Boolean
    ) : BaseDataState {

        data class User(
            val role: UserRole,
            val userName: String
        )

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object UpdateData : Action
        data object CafeClick : Action
        data object SettingsClick : Action
        data object StatisticClick : Action
        data object AcceptOrdersClick : Action
        data object ConfirmAcceptOrders : Action
        data object CancelAcceptOrders : Action
        data object LogoutClick : Action
        data class LogoutConfirm(val confirmed: Boolean) : Action
    }

    sealed interface Event : BaseEvent {
        data object OpenSettings : Event
        data object OpenCafeList : Event
        data object OpenStatistic : Event
        data object OpenLogout : Event
        data object OpenLogin : Event
    }
}
