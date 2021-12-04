package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.domain.model.order.Order

interface IServerOrderMapper {
    fun toModel(order: ServerOrder): Order
}