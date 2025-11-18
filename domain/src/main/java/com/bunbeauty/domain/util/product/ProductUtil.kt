package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.cartproduct.OrderProduct

class ProductUtil : IProductUtil {
    override fun getNewTotalCost(orderProductList: List<OrderProduct>): Int =
        orderProductList
            .map { cartProduct ->
                getCartProductNewCost(cartProduct)
            }.sum()

    override fun getOldTotalCost(orderProductList: List<OrderProduct>): Int? {
        val hasSomeDiscounts =
            orderProductList.any { cartProduct ->
                cartProduct.oldPrice != null
            }

        return if (hasSomeDiscounts) {
            orderProductList
                .map { cartProduct ->
                    getCartProductOldCost(cartProduct) ?: getCartProductNewCost(cartProduct)
                }.sum()
        } else {
            null
        }
    }

    override fun getCartProductNewCost(orderProduct: OrderProduct): Int = orderProduct.newPrice * orderProduct.count

    override fun getCartProductOldCost(orderProduct: OrderProduct): Int? {
        orderProduct.oldPrice?.let {
            return it * orderProduct.count
        }
        return null
    }

    override fun getMenuProductNewPrice(orderProduct: OrderProduct): Int = orderProduct.newPrice

    override fun getMenuProductOldPrice(orderProduct: OrderProduct): Int? = orderProduct.oldPrice

    override fun getPositionName(orderProduct: OrderProduct): String = orderProduct.comboDescription ?: orderProduct.name
}
