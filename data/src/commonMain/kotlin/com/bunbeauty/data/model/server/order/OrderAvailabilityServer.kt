package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
class OrderAvailabilityServer(
    val isAvailable: Boolean,
)
