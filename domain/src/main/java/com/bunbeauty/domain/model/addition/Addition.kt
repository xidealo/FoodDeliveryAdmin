package com.bunbeauty.domain.model.addition

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Addition(
    val uuid: String,
    val name: String,
    val priority: Int,
    val fullName: String?,
    val price: Int?,
    val photoLink: String,
    val isVisible: Boolean,
) : Parcelable