package com.bunbeauty.domain.model.cart_product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderProduct(
    val uuid: String,
    val count: Int,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val utils: String,
    val nutrition: Int,
    val description: String,
    val comboDescription: String?,
    val barcode: Int,
) : Parcelable