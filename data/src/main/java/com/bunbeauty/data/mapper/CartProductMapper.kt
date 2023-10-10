package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.CartProductServer
import com.bunbeauty.domain.model.Addition
import com.bunbeauty.domain.model.cartproduct.OrderProduct
import javax.inject.Inject

class CartProductMapper @Inject constructor() {
    fun toModel(cartProductServer: CartProductServer): OrderProduct {
        return OrderProduct(
            uuid = cartProductServer.uuid,
            count = cartProductServer.count,
            name = cartProductServer.name,
            newPrice = cartProductServer.newPrice,
            oldPrice = cartProductServer.oldPrice,
            utils = cartProductServer.utils,
            nutrition = cartProductServer.nutrition,
            description = cartProductServer.description,
            comboDescription = cartProductServer.comboDescription,
            barcode = cartProductServer.barcode,
            additions = cartProductServer.additions.map { additionServer ->
                Addition(
                    uuid = additionServer.uuid,
                    name = additionServer.name
                )
            },
            newTotalCost = cartProductServer.newTotalCost,
            additionsPrice = cartProductServer.additionsPrice
        )
    }
}