package com.bunbeauty.shared.feature.order.state

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface OrderDetailsState {
    data class DataState(
        val state: State,
        val code: String,
        val orderDetails: OrderDetails?,
        val saving: Boolean,
        val showStatusList: Boolean,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val orderUuid: String,
            val orderCode: String,
        ) : Action

        data object OnStatusClicked : Action

        data object OnCloseStatusClicked : Action

        data class OnSelectStatusClicked(
            val status: OrderStatus,
        ) : Action

        data object OnSaveClicked : Action

        data object OnBackClicked : Action
    }

    sealed interface Event : BaseEvent {
        data object OpenWarningDialogEvent : Event

        data class ShowErrorMessage(
            val messageId: Int,
        ) : Event

        data object GoBackEvent : Event

        data class SavedEvent(
            val orderCode: String,
        ) : Event
    }
}
