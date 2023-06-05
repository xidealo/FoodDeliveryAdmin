package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.presentation.Option

sealed interface OrderDetailsEvent {
    class OpenStatusListEvent(val statusList: List<Option>) : OrderDetailsEvent
    object OpenWarningDialogEvent : OrderDetailsEvent
    object GoBackEvent : OrderDetailsEvent
}