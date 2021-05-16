package com.bunbeauty.data.model

import android.os.Parcelable
import com.bunbeauty.data.model.order.Order
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Statistic(
    override var uuid: String = UUID.randomUUID().toString(),
    var title: String,
    var orderList: List<Order>
) : Parcelable, BaseDiffUtilModel