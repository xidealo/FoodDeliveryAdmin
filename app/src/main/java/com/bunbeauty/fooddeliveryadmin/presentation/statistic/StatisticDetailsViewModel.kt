package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.cost.ICostUtil
import com.bunbeauty.domain.order.IOrderUtil
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import javax.inject.Inject

abstract class StatisticDetailsViewModel : BaseViewModel() {

    abstract val period: String
    abstract val proceeds: String
    abstract val orderCount: String
    abstract val averageCheck: String

    abstract fun getStatisticInfo(orderList: List<Order>): String
}

class StatisticDetailsViewModelImpl @Inject constructor(
    private val args: StatisticDetailsFragmentArgs,
    private val costUtil: ICostUtil,
    private val resourcesProvider: IResourcesProvider,
    private val stringUtil: IStringUtil,
    private val orderUtil: IOrderUtil,
) : StatisticDetailsViewModel() {

    override val period: String
        get() = args.statistic.period

    override val proceeds: String
        get() {
            val proceeds = orderUtil.getProceeds(args.statistic.orderList)
            return stringUtil.getCostString(proceeds)
        }

    override val orderCount: String
        get() = args.statistic.orderList.size.toString()

    override val averageCheck: String
        get() {
            val averageCheck = orderUtil.getAverageCheck(args.statistic.orderList)
            return stringUtil.getCostString(averageCheck)
        }

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