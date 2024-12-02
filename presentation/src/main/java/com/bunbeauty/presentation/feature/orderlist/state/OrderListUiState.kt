package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.domain.enums.OrderStatus

data class OrderListUiState(
    val state: State,
    val connectionError: Boolean,
    val refreshing: Boolean,
) {

    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val cafeAddress: String,
            val orderList: List<OrderItem>
        ) : State
    }

    data class OrderItem(
        val uuid: String,
        val status: OrderStatus,
        val statusString: String,
        val code: String,
        val deferredTime: String,
        val dateTime: String
    )
}
