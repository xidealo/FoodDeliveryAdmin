package com.bunbeauty.domain.model.cafe

sealed interface CafeStatus {
    data object Open: CafeStatus
    data object Closed: CafeStatus
    data class CloseSoon(val minutes: Int): CafeStatus
}