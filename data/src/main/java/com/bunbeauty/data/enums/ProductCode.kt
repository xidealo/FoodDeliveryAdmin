package com.bunbeauty.data.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ProductCode : Parcelable {
    ALL,
    PIZZA,
    BARBECUE,
    BURGER,
    DRINK,
    POTATO,
    SPICE,
    BAKERY,
    COMBO,
    OVEN
}