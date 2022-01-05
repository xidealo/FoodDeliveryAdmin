package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.ServerCartProduct
import com.bunbeauty.domain.model.cart_product.CartProduct
import javax.inject.Inject

class CartProductMapper @Inject constructor() {
    fun toModel(serverCartProduct: ServerCartProduct): CartProduct {
       return CartProduct(
            uuid = serverCartProduct.uuid,
            count = serverCartProduct.count,
            name = serverCartProduct.name,
            newPrice = serverCartProduct.newPrice,
            oldPrice = serverCartProduct.oldPrice,
            utils = serverCartProduct.utils,
            nutrition = serverCartProduct.nutrition,
            description = serverCartProduct.description,
            comboDescription = serverCartProduct.comboDescription,
            barcode = serverCartProduct.barcode,
        )
    }
}