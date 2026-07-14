package com.bunbeauty.domain.feature.clientuser.model

data class ClientUserStatistic(
    val phoneNumber: String,
    val firstOrderDate: Long?,
    val lastOrderDate: Long?,
    val deliveryOrderCount: Int,
    val pickupOrderCount: Int,
    val orderCount: Int,
    val averageCheck: Double,
)
