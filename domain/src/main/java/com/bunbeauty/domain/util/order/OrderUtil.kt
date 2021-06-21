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
        val orderCost = productUtil.getNewTotalCost(order.cartProducts)

        return if (order.orderEntity.isDelivery && orderCost < delivery.forFree) {
            delivery.cost
        } else {
            0
        }
    }

    override fun getOldOrderCost(order: Order, delivery: Delivery): Int? {
        val orderCost = productUtil.getOldTotalCost(order.cartProducts) ?: return null
        val deliveryCost = getDeliveryCost(order, delivery)

        return orderCost + deliveryCost
    }

    override fun getNewOrderCost(order: Order, delivery: Delivery): Int {
        val orderCost = productUtil.getNewTotalCost(order.cartProducts)
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
            order.cartProducts
        }
        val productStatisticList = ArrayList<ProductStatistic>()
        cartProductList.forEach { cartProduct ->
            val positionName = productUtil.getPositionName(cartProduct.menuProduct)
            val foundProductStatistic = productStatisticList.find { productStatistic ->
                productStatistic.name == positionName
            }
            if (foundProductStatistic == null) {
                productStatisticList.add(
                    ProductStatistic(
                        name = positionName,
                        orderCount = 1,
                        count = cartProduct.count,
                        cost = productUtil.getCartProductNewCost(cartProduct),
                    )
                )
            } else {
                foundProductStatistic.apply {
                    orderCount++
                    count += cartProduct.count
                    cost += productUtil.getCartProductNewCost(cartProduct)
                }
            }
        }

        return productStatisticList.sortedByDescending { productStatistic ->
            productStatistic.count
        }
    }
}