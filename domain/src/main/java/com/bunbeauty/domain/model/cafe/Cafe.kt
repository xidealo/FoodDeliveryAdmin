package com.bunbeauty.domain.model.cafe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cafe(
    val uuid: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val fromTime: Int,
    val toTime: Int,
    val phone: String,
    val visible: Boolean,
    val cityUuid: String
) : Parcelable