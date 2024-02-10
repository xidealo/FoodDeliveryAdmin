package com.bunbeauty.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Addition(
    val uuid: String,
    val name: String,
    val priority: Int,
) : Parcelable