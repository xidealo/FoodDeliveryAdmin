package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.model.server.order.OrderAvailabilityServer
import com.bunbeauty.domain.feature.order.model.OrderAvailability

fun OrderAvailabilityServer.toOrderAvailability(): OrderAvailability {
    return OrderAvailability(isAvailable = isAvailable)
}