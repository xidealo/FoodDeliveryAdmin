package com.bunbeauty.data.model

import android.os.Parcelable
import com.bunbeauty.data.model.order.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    override var uuid: String = "",
    var date: String,
    var orderList: List<Order>
) : Parcelable, BaseDiffUtilModel