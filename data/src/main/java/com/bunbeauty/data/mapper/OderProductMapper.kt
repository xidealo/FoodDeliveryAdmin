package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.OrderProductServer
import com.bunbeauty.domain.model.Addition
import com.bunbeauty.domain.model.cartproduct.OrderProduct
import javax.inject.Inject

class OderProductMapper @Inject constructor() {
    fun toModel(orderProductServer: OrderProductServer): OrderProduct {
        return OrderProduct(
            uuid = orderProductServer.uuid,
            count = orderProductServer.count,
            name = orderProductServer.name,
            newPrice = orderProductServer.newPrice,
            oldPrice = orderProductServer.oldPrice,
            utils = orderProductServer.utils,
            nutrition = orderProductServer.nutrition,
            description = orderProductServer.description,
            comboDescription = orderProductServer.comboDescription,
            barcode = orderProductServer.barcode,
            additions = orderProductServer.additions.map { additionServer ->
                Addition(
                    uuid = additionServer.uuid,
                    name = additionServer.name,
                    priority = additionServer.priority,
                )
            },
            newTotalCost = orderProductServer.newTotalCost,
            additionsPrice = orderProductServer.additionsPrice
        )
    }
}