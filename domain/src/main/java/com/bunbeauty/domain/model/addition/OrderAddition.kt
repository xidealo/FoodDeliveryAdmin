package com.bunbeauty.domain.model.addition

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderAddition(
    val uuid: String,
    val name: String,
    val priority: Int,
) : Parcelable