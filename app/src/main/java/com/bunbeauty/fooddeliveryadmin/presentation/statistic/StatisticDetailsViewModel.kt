package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.ProductStatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

abstract class StatisticDetailsViewModel : BaseViewModel() {

    abstract val period: String
    abstract val proceeds: String
    abstract val orderCount: String
    abstract val averageCheck: String
    abstract val productStatisticList: List<ProductStatisticItem>
}

class StatisticDetailsViewModelImpl @Inject constructor(
    private val args: StatisticDetailsFragmentArgs,
    private val stringUtil: IStringUtil,
    private val orderUtil: IOrderUtil,
    dataStoreRepo: DataStoreRepo,
) : StatisticDetailsViewModel() {

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    override val period: String
        get() = args.statistic.period

    override val proceeds: String
        get() {
            val proceeds = orderUtil.getProceeds(args.statistic.orderList, delivery)
            return stringUtil.getCostString(proceeds)
        }

    override val orderCount: String
        get() = args.statistic.orderList.size.toString()

    override val averageCheck: String
        get() {
            val averageCheck = orderUtil.getAverageCheck(args.statistic.orderList, delivery)
            return stringUtil.getCostString(averageCheck)
        }

    override val productStatisticList: List<ProductStatisticItem>
        get() = orderUtil.getProductStatisticList(args.statistic.orderList)
            .map { productStatistic ->
                ProductStatisticItem(
                    name = productStatistic.name,
                    orderCount = productStatistic.orderCount.toString(),
                    count = productStatistic.count.toString(),
                    cost = stringUtil.getCostString(productStatistic.cost)
                )
            }
}