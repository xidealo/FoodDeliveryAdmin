package com.bunbeauty.domain.product

import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.MenuProduct
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

    override fun getDifferenceBeforeFreeDelivery(
        cartProductList: List<CartProduct>,
        priceForFreeDelivery: Int
    ): Int {
        return priceForFreeDelivery - getNewTotalCost(cartProductList)
    }



    override fun getPositionName(menuProduct: MenuProduct): String {
        return if (menuProduct.comboDescription.isEmpty()) {
            menuProduct.name
        } else {
            menuProduct.comboDescription
        }
    }
}