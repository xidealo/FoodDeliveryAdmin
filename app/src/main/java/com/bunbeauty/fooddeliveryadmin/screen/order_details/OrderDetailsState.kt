package com.bunbeauty.fooddeliveryadmin.screen.order_details

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.fooddeliveryadmin.core_ui.ListItem
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option

data class OrderDetailsState(
    val orderDetails: OrderDetails? = null,
    val itemModelList: List<ListItem> = emptyList(),
    val deliveryCost: String? = null,
    val discount: String? = null,
    val oldFinalCost: String? = null,
    val newFinalCost: String? = null,
    val selectedStatus: OrderStatus? = null,
    val isLoading: Boolean = false,
    val eventList: List<Event> = emptyList()
) {

    sealed interface Event {
        class OpenStatusListEvent(val statusList: List<Option>) : Event
        object OpenWarningDialogEvent : Event
        class OpenErrorDialogEvent(val retryAction: RetryAction) : Event
        object GoBackEvent : Event
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)
    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}

enum class RetryAction {
    LOAD_ORDER,
    SAVE_STATUS,
}
