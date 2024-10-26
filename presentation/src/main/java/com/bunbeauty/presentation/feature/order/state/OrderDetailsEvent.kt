package com.bunbeauty.presentation.feature.order.state

import com.bunbeauty.presentation.Option

sealed interface OrderDetailsEvent {
    data class OpenStatusListEvent(val statusList: List<Option>) : OrderDetailsEvent
    data object OpenWarningDialogEvent : OrderDetailsEvent
    data class ShowErrorMessage(val message: String) : OrderDetailsEvent
    data object GoBackEvent : OrderDetailsEvent
    data class SavedEvent(val orderCode: String) : OrderDetailsEvent
}
