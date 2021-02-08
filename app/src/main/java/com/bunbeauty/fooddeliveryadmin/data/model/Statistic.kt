package com.bunbeauty.fooddeliveryadmin.data.model

import android.os.Parcelable
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    override var uuid: String = "",
    var date: String,
    var order: List<Order>
) : Parcelable, BaseModel()