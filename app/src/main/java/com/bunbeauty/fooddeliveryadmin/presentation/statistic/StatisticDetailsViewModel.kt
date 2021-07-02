package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.ProductStatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StatisticDetailsViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val orderUtil: IOrderUtil,
    dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val args: StatisticDetailsFragmentArgs by savedStateHandle.navArgs()
    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val period: String
        get() = args.statistic.period

    val proceeds: String
        get() {
            val proceeds = orderUtil.getProceeds(args.statistic.orderList, delivery)
            return stringUtil.getCostString(proceeds)
        }

    val orderCount: String
        get() = args.statistic.orderList.size.toString()

    val averageCheck: String
        get() {
            val averageCheck = orderUtil.getAverageCheck(args.statistic.orderList, delivery)
            return stringUtil.getCostString(averageCheck)
        }

    val productStatisticList: List<ProductStatisticItem>
        get() = orderUtil.getProductStatisticList(args.statistic.orderList)
            .map { productStatistic ->
                ProductStatisticItem(
                    name = productStatistic.name,
                    photoLink = productStatistic.photoLink,
                    orderCount = productStatistic.orderCount.toString(),
                    count = productStatistic.count.toString(),
                    cost = stringUtil.getCostString(productStatistic.cost)
                )
            }
}