package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.model.server.order.ServerOrderDetails
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails

interface IServerOrderMapper {
    fun toModel(order: ServerOrderDetails): OrderDetails
    fun toModel(order: ServerOrder): Order
}