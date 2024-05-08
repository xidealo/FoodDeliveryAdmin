package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.domain.model.order.details.OrderDetails

data class OrderDetailsDataState(
    val state: State,
    val code: String,

    val orderDetails: OrderDetails?,

    val eventList: List<OrderDetailsEvent>
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    operator fun plus(event: OrderDetailsEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<OrderDetailsEvent>) =
        copy(eventList = eventList - events.toSet())

    companion object {

        fun crateInitialOrderDetailsDataState() = OrderDetailsDataState(
            state = State.LOADING,
            code = "",
            orderDetails = null,
            eventList = emptyList()
        )
    }
}
