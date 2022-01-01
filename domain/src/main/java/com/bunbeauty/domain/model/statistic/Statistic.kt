package com.bunbeauty.domain.model.statistic

import android.os.Parcelable
import com.bunbeauty.domain.model.order.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    val period: String,
    val startPeriodTime: Long,
    val orderCount: Int,
    val proceeds: Int,
    val averageCheck: Int,
    val productStatisticList: List<ProductStatistic>
) : Parcelable