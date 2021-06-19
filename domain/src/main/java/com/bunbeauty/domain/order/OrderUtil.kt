package com.bunbeauty.domain.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.product.IProductUtil
import javax.inject.Inject

class OrderUtil @Inject constructor(
    private val productUtil: IProductUtil
) : IOrderUtil {

    override fun getProceeds(orderList: List<Order>): Int {
        val cartProductList = orderList.flatMap { order ->
            order.cartProducts
        }

        return productUtil.getNewTotalCost(cartProductList)
    }

    override fun getAverageCheck(orderList: List<Order>): Int {
        val proceeds = getProceeds(orderList)

        return proceeds / orderList.size
    }
}