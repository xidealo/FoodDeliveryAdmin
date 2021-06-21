package com.bunbeauty.domain.model.statistic

import android.os.Parcelable
import com.bunbeauty.domain.model.order.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    var period: String,
    var orderList: List<Order>
) : Parcelable