package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.product.IProductUtil
import javax.inject.Inject

class OrderUtil @Inject constructor(
    private val productUtil: IProductUtil
) : IOrderUtil {

    override fun getOldOrderCost(order: Order): Int? {
        val orderCost = productUtil.getOldTotalCost(order.oderProductList) ?: return null
        return orderCost + (order.deliveryCost ?: 0)
    }

    override fun getNewOrderCost(order: Order): Int {
        val orderCost = productUtil.getNewTotalCost(order.oderProductList)
        val bonusDiscount = order.discount ?: 0

        return orderCost + (order.deliveryCost ?: 0) - bonusDiscount
    }


}