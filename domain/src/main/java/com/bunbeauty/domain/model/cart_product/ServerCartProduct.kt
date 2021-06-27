package com.bunbeauty.domain.model.cart_product

import com.bunbeauty.domain.model.ServerMenuProduct

data class ServerCartProduct(
    val count: Int = 0,
    val menuProduct: ServerMenuProduct = ServerMenuProduct()
)
