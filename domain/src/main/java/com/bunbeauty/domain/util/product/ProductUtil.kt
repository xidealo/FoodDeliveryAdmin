package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.cart_product.CartProduct
import javax.inject.Inject

class ProductUtil @Inject constructor() : IProductUtil {

    override fun getNewTotalCost(cartProductList: List<CartProduct>): Int {
        return cartProductList.map { cartProduct ->
            getCartProductNewCost(cartProduct)
        }.sum()
    }

    override fun getOldTotalCost(cartProductList: List<CartProduct>): Int? {
        val hasSomeDiscounts = cartProductList.any { cartProduct ->
            cartProduct.oldPrice != null
        }

        return if (hasSomeDiscounts) {
            cartProductList.map { cartProduct ->
                getCartProductOldCost(cartProduct) ?: getCartProductNewCost(cartProduct)
            }.sum()
        } else {
            null
        }
    }

    override fun getCartProductNewCost(cartProduct: CartProduct): Int {
        return cartProduct.newPrice * cartProduct.count
    }

    override fun getCartProductOldCost(cartProduct: CartProduct): Int? {
        cartProduct.oldPrice?.let {
            return it * cartProduct.count
        }
        return null
    }

    override fun getMenuProductNewPrice(cartProduct: CartProduct): Int {
        return cartProduct.newPrice
    }

    override fun getMenuProductOldPrice(cartProduct: CartProduct): Int? {
        return cartProduct.oldPrice
    }

    override fun getPositionName(cartProduct: CartProduct): String {
        return cartProduct.comboDescription ?: cartProduct.name
    }
}