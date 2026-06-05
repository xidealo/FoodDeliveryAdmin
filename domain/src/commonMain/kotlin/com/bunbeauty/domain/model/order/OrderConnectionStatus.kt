package com.bunbeauty.domain.model.order

sealed interface OrderConnectionStatus {
    data object Connecting : OrderConnectionStatus

    data object Connected : OrderConnectionStatus

    data class Error(
        val message: String,
    ) : OrderConnectionStatus
}
