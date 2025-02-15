package com.bunbeauty.presentation.feature.settings.state

import androidx.annotation.StringRes
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState.Event
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface SettingsState {

    data class DataState(
        val state: State,
        val isUnlimitedNotifications: Boolean,
        val workType: WorkType,
        val isLoading: Boolean,
        val showAcceptOrdersConfirmation: Boolean
    ) : BaseDataState {

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }

        enum class WorkType {
            DELIVERY,
            PICKUP,
            DELIVERY_AND_PICKUP,
            CLOSED
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnBackClicked : Action
        data class OnNotificationsClicked(val isUnlimitedNotifications: Boolean) : Action
        data object OnSaveSettingsClick : Action
        data class OnSelectStatusClicked(val workType: DataState.WorkType) : Action
        data object CancelAcceptOrders : Action
        data object ConfirmNotAcceptOrders : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data object ShowSaveSettingEvent : Event
        data class ShowErrorMessage(@StringRes val messageId: Int) : Event
    }
}
