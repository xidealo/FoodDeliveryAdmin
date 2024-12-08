package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface OrderList {
    data class DataState(
        val refreshing: Boolean,
        val selectedCafe: SelectedCafe?,
        val orderListState: State,
        val orderList: List<Order>,
        val hasConnectionError: Boolean,
        val cafeList: List<SelectableCafeItem>,
        val showCafeList: Boolean
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object StartObserveOrders : Action
        data object StopObserveOrders : Action
        data object CloseCafeListBottomSheet : Action
        data object RefreshSwipe : Action
        data object RetryClick : Action
        data object CafeClick : Action
        data class SelectedCafe(val cafeUuid: String) : Action
        data class OrderClick(val orderUuid: String, val orderCode: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object ScrollToTop : Event
        data class OpenOrderDetailsEvent(val orderUuid: String, val orderCode: String) : Event
        data class CancelNotification(val notificationId: Int) : Event
    }
}
