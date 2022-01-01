package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.statistic.ProductStatistic
import com.bunbeauty.domain.util.product.IProductUtil
import javax.inject.Inject

class OrderUtil @Inject constructor(
    private val productUtil: IProductUtil
) : IOrderUtil {

    override fun getDeliveryCost(order: Order, delivery: Delivery): Int {
        val orderCost = productUtil.getNewTotalCost(order.cartProductList)

        return if (order.delivery && orderCost < delivery.forFree) {
            delivery.cost
        } else {
            0
        }
    }

    override fun getOldOrderCost(order: Order, delivery: Delivery): Int? {
        val orderCost = productUtil.getOldTotalCost(order.cartProductList) ?: return null
        val deliveryCost = getDeliveryCost(order, delivery)

        return orderCost + deliveryCost
    }

    override fun getNewOrderCost(order: Order, delivery: Delivery): Int {
        val orderCost = productUtil.getNewTotalCost(order.cartProductList)
        val deliveryCost = getDeliveryCost(order, delivery)
        val bonusDiscount = order.bonus ?: 0

        return orderCost + deliveryCost - bonusDiscount
    }


}