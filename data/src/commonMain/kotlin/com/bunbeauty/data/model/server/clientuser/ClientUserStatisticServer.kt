package com.bunbeauty.data.model.server.clientuser

import kotlinx.serialization.Serializable

@Serializable
data class ClientUserStatisticServer(
    val phoneNumber: String,
    val firstOrderDate: Long?,
    val lastOrderDate: Long?,
    val deliveryOrderCount: Int,
    val pickupOrderCount: Int,
    val orderCount: Int,
    val averageCheck: Double,
    val isProblematic: Boolean,
)
