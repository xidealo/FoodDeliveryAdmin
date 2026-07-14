package com.bunbeauty.data.mapper.clientuser

import com.bunbeauty.data.model.server.clientuser.ClientUserStatisticServer
import com.bunbeauty.domain.feature.clientuser.model.ClientUserStatistic

class ClientUserStatisticMapper {
    fun map(clientUserStatisticServer: ClientUserStatisticServer): ClientUserStatistic =
        ClientUserStatistic(
            phoneNumber = clientUserStatisticServer.phoneNumber,
            firstOrderDate = clientUserStatisticServer.firstOrderDate,
            lastOrderDate = clientUserStatisticServer.lastOrderDate,
            deliveryOrderCount = clientUserStatisticServer.deliveryOrderCount,
            pickupOrderCount = clientUserStatisticServer.pickupOrderCount,
            orderCount = clientUserStatisticServer.orderCount,
            averageCheck = clientUserStatisticServer.averageCheck,
        )
}
