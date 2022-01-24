package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class DeliveryServer(
    val cost: Int = 0,
    val forFree: Int = 0
)