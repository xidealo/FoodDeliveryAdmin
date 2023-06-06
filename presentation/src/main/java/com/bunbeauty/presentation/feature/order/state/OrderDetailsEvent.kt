package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.presentation.Option

sealed interface OrderDetailsEvent {
    data class OpenStatusListEvent(val statusList: List<Option>) : OrderDetailsEvent
    object OpenWarningDialogEvent : OrderDetailsEvent
    data class ShowErrorMessage(val message: String) : OrderDetailsEvent
    object GoBackEvent : OrderDetailsEvent
}