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

        return orderCost + deliveryCost
    }

    override fun getProceeds(orderList: List<Order>, delivery: Delivery): Int {
        return orderList.sumBy { order ->
            getNewOrderCost(order, delivery)
        }
    }

    override fun getAverageCheck(orderList: List<Order>, delivery: Delivery): Int {
        val proceeds = getProceeds(orderList, delivery)

        return proceeds / orderList.size
    }

    override fun getProductStatisticList(orderList: List<Order>): List<ProductStatistic> {
        val cartProductList = orderList.flatMap { order ->
            order.cartProductList
        }
        val productStatisticList = ArrayList<ProductStatistic>()
        cartProductList.forEach { cartProduct ->
            val positionName = productUtil.getPositionName(cartProduct.menuProduct)
            val foundProductStatistic = productStatisticList.find { productStatistic ->
                productStatistic.name == positionName
            }
            foundProductStatistic?.apply {
                orderCount++
                count += cartProduct.count
                cost += productUtil.getCartProductNewCost(cartProduct)
            } ?: productStatisticList.add(
                ProductStatistic(
                    name = positionName,
                    photoLink = cartProduct.menuProduct.photoLink,
                    orderCount = 1,
                    count = cartProduct.count,
                    cost = productUtil.getCartProductNewCost(cartProduct),
                )
            )
        }

        return productStatisticList.sortedByDescending { productStatistic ->
            productStatistic.count
        }
    }
}