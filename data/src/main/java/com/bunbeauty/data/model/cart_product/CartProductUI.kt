package com.bunbeauty.data.model.cart_product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProductUI(
    val name: String,
    var photoLink: String,
    var count: String,
    var newCost: String,
    var oldCost: String,
) : Parcelable