package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.details.OrderDetails

interface IServerOrderMapper {
    fun mapOrderDetails(orderDetailsServer: OrderDetailsServer): OrderDetails

    fun mapOrder(orderServer: OrderServer): Order
}
