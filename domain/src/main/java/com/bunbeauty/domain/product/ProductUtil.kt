package com.bunbeauty.domain.product

import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.domain.string.IStringUtil
import javax.inject.Inject

class ProductUtil @Inject constructor(private val stringUtil: IStringUtil) : IProductUtil {


    override fun getNewTotalCost(cartProductList: List<CartProduct>): Int {
        return cartProductList.map { cartProduct ->
            getCartProductNewCost(cartProduct)
        }.sum()
    }

    override fun getOldTotalCost(cartProductList: List<CartProduct>): Int {
        return cartProductList.map { cartProduct ->
            getCartProductOldCost(cartProduct)
        }.sum()
    }

    override fun getCartProductNewCost(cartProduct: CartProduct): Int {
        return getMenuProductNewPrice(cartProduct.menuProduct) * cartProduct.count
    }

    override fun getCartProductOldCost(cartProduct: CartProduct): Int {
        return cartProduct.menuProduct.cost * cartProduct.count
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