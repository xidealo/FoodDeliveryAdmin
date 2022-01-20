package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.CartProductServer
import com.bunbeauty.domain.model.cart_product.CartProduct
import javax.inject.Inject

class CartProductMapper @Inject constructor() {
    fun toModel(cartProductServer: CartProductServer): CartProduct {
       return CartProduct(
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
        )
    }
}