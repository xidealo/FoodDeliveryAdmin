package com.bunbeauty.fooddeliveryadmin.presentation

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.cost.ICostUtil
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

abstract class SelectedStatisticViewModel : BaseViewModel() {

    abstract fun getStatisticInfo(orderList: List<Order>): String
}

class SelectedStatisticViewModelImpl @Inject constructor(
    private val costUtil: ICostUtil,
    private val resourcesProvider: IResourcesProvider,
) : SelectedStatisticViewModel() {

    override fun getStatisticInfo(orderList: List<Order>): String {
        val statisticStringBuilder = StringBuilder()
        //TODO (change calculating)
        statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_statistic_proceeds))
        statisticStringBuilder.append(
            orderList.sumBy { order ->
                order.cartProducts.sumBy { cartProduct ->
                    costUtil.getCost(cartProduct)
                }
            }.toString()
        )
        statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_ruble))
        statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_break))

        statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_statistic_orders_count))
        statisticStringBuilder.append(orderList.size.toString())
        statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_break))

        orderList.flatMap { order ->
            order.cartProducts
        }.groupBy { cartProduct ->
            cartProduct.menuProduct.name
        }.toList()
            .sortedByDescending { nameToCartProductPair ->
                nameToCartProductPair.second.sumBy { cartProduct ->
                    cartProduct.count
                }
            }.forEachIndexed { i, nameToCartProductPair ->
                statisticStringBuilder.append((i + 1).toString())
                statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_parenthesis))
                statisticStringBuilder.append(nameToCartProductPair.first)
                statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_colon))
                statisticStringBuilder.append(
                    nameToCartProductPair.second.sumBy { cartProduct ->
                        cartProduct.count
                    }.toString()
                )
                statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_pieces))
                statisticStringBuilder.append(resourcesProvider.getString(R.string.msg_break))
            }

        return statisticStringBuilder.toString()
    }
}
