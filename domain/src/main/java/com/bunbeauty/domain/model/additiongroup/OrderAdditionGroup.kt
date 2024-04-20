package com.bunbeauty.domain.model.additiongroup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderAdditionGroup(
    val uuid: String,
    val name: String,
    val priority: Int,
) : Parcelable