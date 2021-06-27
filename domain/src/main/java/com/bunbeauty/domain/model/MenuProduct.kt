package com.bunbeauty.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProduct(
    val uuid: String,
    val name: String,
    val cost: Int,
    val discountCost: Int?,
    val weight: Int,
    val description: String,
    val comboDescription: String,
    val photoLink: String,
    val onFire: Boolean,
    val inOven: Boolean,
    val productCode: String,
    val barcode: Int,
    val visible: Boolean,
): Parcelable