package com.bunbeauty.domain.model.statistic

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    val period: String,
    val startPeriodTime: Long,
    val orderCount: Int,
    val proceeds: Int,
    val currency: String
) : Parcelable