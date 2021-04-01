package com.bunbeauty.domain.cost_helper

import com.bunbeauty.data.model.CartProduct
import javax.inject.Inject

class CostHelper @Inject constructor() : ICostHelper {

    override fun getCost(cartProduct: CartProduct): Int {
        return if (cartProduct.menuProduct.discountCost != null) {
            cartProduct.count * cartProduct.menuProduct.discountCost!!
        } else {
            cartProduct.count * cartProduct.menuProduct.cost
        }
    }
}