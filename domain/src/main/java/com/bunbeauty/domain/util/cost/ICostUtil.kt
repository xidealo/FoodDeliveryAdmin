package com.bunbeauty.domain.util.cost

import com.bunbeauty.domain.model.cart_product.CartProduct

interface ICostUtil {
    fun getCost(cartProduct: CartProduct): Int
    fun getOldCost(oldCost: Int, newCost: Int, rubles: String): String
}