package com.bunbeauty.fooddeliveryadmin.enums

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
    OVEN
}