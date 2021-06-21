package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.cart_product.CartProduct
import com.bunbeauty.domain.model.MenuProduct

interface IProductUtil {

    fun getNewTotalCost(cartProductList: List<CartProduct>): Int
    fun getOldTotalCost(cartProductList: List<CartProduct>): Int?

    fun getCartProductNewCost(cartProduct: CartProduct): Int
    fun getCartProductOldCost(cartProduct: CartProduct): Int?

    fun getMenuProductNewPrice(menuProduct: MenuProduct): Int

    fun getPositionName(menuProduct: MenuProduct): String
}