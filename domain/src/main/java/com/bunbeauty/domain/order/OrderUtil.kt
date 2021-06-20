package com.bunbeauty.domain.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.statistic.ProductStatistic
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