package com.bunbeauty.data.model.order

import android.os.Parcelable
import com.bunbeauty.data.model.order.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    var period: String,
    var orderList: List<Order>
) : Parcelable