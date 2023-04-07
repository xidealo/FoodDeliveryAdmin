package com.bunbeauty.fooddeliveryadmin.screen.option_list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Option(
    val id: String?,
    val title: String,
    val isPrimary: Boolean = false
) : Parcelable
