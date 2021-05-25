package com.bunbeauty.domain.cost

import com.bunbeauty.data.model.cart_product.CartProduct

interface ICostUtil {
    fun getCost(cartProduct: CartProduct): Int
    fun getOldCost(oldCost: Int, newCost: Int, rubles: String): String
}