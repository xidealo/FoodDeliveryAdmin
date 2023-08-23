package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.cartproduct.OrderProduct
import javax.inject.Inject

class ProductUtil @Inject constructor() : IProductUtil {

    override fun getNewTotalCost(orderProductList: List<OrderProduct>): Int {
        return orderProductList.map { cartProduct ->
            getCartProductNewCost(cartProduct)
        }.sum()
    }

    override fun getOldTotalCost(orderProductList: List<OrderProduct>): Int? {
        val hasSomeDiscounts = orderProductList.any { cartProduct ->
            cartProduct.oldPrice != null
        }

        return if (hasSomeDiscounts) {
            orderProductList.map { cartProduct ->
                getCartProductOldCost(cartProduct) ?: getCartProductNewCost(cartProduct)
            }.sum()
        } else {
            null
        }
    }

    override fun getCartProductNewCost(orderProduct: OrderProduct): Int {
        return orderProduct.newPrice * orderProduct.count
    }

    override fun getCartProductOldCost(orderProduct: OrderProduct): Int? {
        orderProduct.oldPrice?.let {
            return it * orderProduct.count
        }
        return null
    }

    override fun getMenuProductNewPrice(orderProduct: OrderProduct): Int {
        return orderProduct.newPrice
    }

    override fun getMenuProductOldPrice(orderProduct: OrderProduct): Int? {
        return orderProduct.oldPrice
    }

    override fun getPositionName(orderProduct: OrderProduct): String {
        return orderProduct.comboDescription ?: orderProduct.name
    }
}