package com.bunbeauty.domain.cost

import com.bunbeauty.data.model.cart_product.CartProduct
import javax.inject.Inject

class CostUtil @Inject constructor() : ICostUtil {

    override fun getCost(cartProduct: CartProduct): Int {
        return if (cartProduct.menuProduct.discountCost != null) {
            cartProduct.count * cartProduct.menuProduct.discountCost!!
        } else {
            cartProduct.count * cartProduct.menuProduct.cost
        }
    }

    override fun getOldCost(oldCost: Int, newCost: Int, rubles: String): String {
        return if (oldCost == newCost) {
            ""
        } else {
            oldCost.toString() + rubles
        }
    }


}