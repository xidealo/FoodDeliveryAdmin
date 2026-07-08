package com.bunbeauty.domain.enums

enum class OrderStatus {
    NOT_ACCEPTED,
    ACCEPTED,
    PREPARING,
    SENT_OUT,
    DELIVERED,
    DONE,
    CANCELED,
}

fun OrderStatus.isFinal(): Boolean = this == OrderStatus.DELIVERED || this == OrderStatus.CANCELED
