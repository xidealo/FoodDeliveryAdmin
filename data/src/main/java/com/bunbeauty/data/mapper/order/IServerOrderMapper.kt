package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.Mapper
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.server.ServerOrder

interface IServerOrderMapper: Mapper<ServerOrder, Order>