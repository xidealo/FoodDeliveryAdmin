package com.bunbeauty.domain.product

import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.MenuProduct

interface IProductUtil {

    fun getNewTotalCost(cartProductList: List<CartProduct>): Int
    fun getOldTotalCost(cartProductList: List<CartProduct>): Int?

    fun getCartProductNewCost(cartProduct: CartProduct): Int
    fun getCartProductOldCost(cartProduct: CartProduct): Int?

    fun getMenuProductNewPrice(menuProduct: MenuProduct): Int

    fun getDifferenceBeforeFreeDelivery(cartProductList: List<CartProduct>, priceForFreeDelivery: Int): Int

    fun getPositionName(menuProduct: MenuProduct): String
}