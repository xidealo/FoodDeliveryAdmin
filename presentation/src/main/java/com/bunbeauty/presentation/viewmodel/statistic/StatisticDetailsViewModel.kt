package com.bunbeauty.presentation.viewmodel.statistic

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.common.Constants.STATISTIC_ARGS_KEY
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.ProductStatisticItemModel
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel

class StatisticDetailsViewModel(
    private val stringUtil: IStringUtil,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val statistic: Statistic by savedStateHandle.navArg(STATISTIC_ARGS_KEY)

    val period: String = statistic.period

    val proceeds: String
        get() {
            return stringUtil.getCostString(statistic.proceeds)
        }

    val orderCount: String = statistic.orderCount.toString()

    val averageCheck: String
        get() {
            return stringUtil.getCostString(0)
        }

    val productStatisticList: List<ProductStatisticItemModel>
        get() = emptyList()
}
