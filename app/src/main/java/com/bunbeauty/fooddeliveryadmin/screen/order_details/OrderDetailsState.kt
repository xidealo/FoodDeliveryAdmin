package com.bunbeauty.fooddeliveryadmin.screen.order_details

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.fooddeliveryadmin.core_ui.ListItem
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option

data class OrderDetailsState(
    val order: Order? = null,
    val itemModelList: List<ListItem> = emptyList(),
    val deliveryCost: String? = null,
    val discount: String? = null,
    val oldFinalCost: String? = null,
    val newFinalCost: String? = null,
    val selectedStatus: OrderStatus? = null,
    val isLoading: Boolean? = false,
    val eventList: List<Event> = emptyList()
) {

    sealed class Event
    class OpenStatusListEvent(val statusList: List<Option>) : Event()
    object OpenWarningDialogEvent : Event()
}
