package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface OrderList {
    data class DataState(
        val refreshing: Boolean,
        val orderListState: State,
        val orderList: List<Order>,
        val hasConnectionError: Boolean,
        val cafe: Cafe?,
        val loadingOrderList: Boolean,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
        }
    }

    sealed interface Action : BaseAction {
        data object StartObserveOrders : Action

        data object StopObserveOrders : Action

        data object RefreshSwipe : Action

        data object RetryClick : Action

        data class OrderClick(
            val orderUuid: String,
            val orderCode: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object ScrollToTop : Event

        data class OpenOrderDetailsEvent(
            val orderUuid: String,
            val orderCode: String,
        ) : Event

        data class CancelNotification(
            val notificationId: Int,
        ) : Event
    }
}
