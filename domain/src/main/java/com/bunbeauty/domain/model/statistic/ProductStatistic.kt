package com.bunbeauty.domain.model.statistic

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductStatistic(
    val name: String,
    var orderCount: Int,
    var productCount: Int,
    var proceeds: Int
) : Parcelable
