package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.menu_product.MenuProduct
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
            cartProduct.menuProduct.discountCost != null
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
        return getMenuProductNewPrice(cartProduct.menuProduct) * cartProduct.count
    }

    override fun getCartProductOldCost(cartProduct: CartProduct): Int? {
        return if (cartProduct.menuProduct.discountCost == null) {
            null
        } else {
            cartProduct.menuProduct.cost * cartProduct.count
        }
    }

    override fun getMenuProductNewPrice(menuProduct: MenuProduct): Int {
        return menuProduct.discountCost ?: menuProduct.cost
    }

    override fun getMenuProductOldPrice(menuProduct: MenuProduct): Int? {
        return if (menuProduct.discountCost == null) {
            null
        } else {
            menuProduct.cost
        }
    }

    override fun getPositionName(menuProduct: MenuProduct): String {
        return menuProduct.comboDescription ?: menuProduct.name
    }
}