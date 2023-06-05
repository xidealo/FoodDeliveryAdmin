package com.bunbeauty.domain.util.product

import com.bunbeauty.domain.model.cart_product.OrderProduct

interface IProductUtil {

    fun getNewTotalCost(orderProductList: List<OrderProduct>): Int
    fun getOldTotalCost(orderProductList: List<OrderProduct>): Int?

    fun getCartProductNewCost(orderProduct: OrderProduct): Int
    fun getCartProductOldCost(orderProduct: OrderProduct): Int?

    fun getMenuProductNewPrice(orderProduct: OrderProduct): Int
    fun getMenuProductOldPrice(orderProduct: OrderProduct): Int?

    fun getPositionName(orderProduct: OrderProduct): String
}