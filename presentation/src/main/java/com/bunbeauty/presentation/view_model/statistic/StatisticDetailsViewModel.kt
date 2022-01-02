package com.bunbeauty.presentation.view_model.statistic

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.common.Constants.STATISTIC_ARGS_KEY
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.ProductStatisticItemModel
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
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

    private val statistic: Statistic = savedStateHandle.navArg(STATISTIC_ARGS_KEY)!!

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val period: String = statistic.period

    val proceeds: String
        get() {
            val proceeds = orderUtil.getProceeds(statistic.orderList, delivery)
            return stringUtil.getCostString(proceeds)
        }

    val orderCount: String = statistic.orderList.size.toString()

    val averageCheck: String
        get() {
            val averageCheck = orderUtil.getAverageCheck(statistic.orderList, delivery)
            return stringUtil.getCostString(averageCheck)
        }

    val productStatisticList: List<ProductStatisticItemModel>
        get() = orderUtil.getProductStatisticList(statistic.orderList)
            .map { productStatistic ->
                ProductStatisticItemModel(
                    name = productStatistic.name,
                    photoLink = productStatistic.photoLink,
                    orderCount = productStatistic.orderCount.toString(),
                    count = productStatistic.count.toString(),
                    cost = stringUtil.getCostString(productStatistic.cost)
                )
            }
}