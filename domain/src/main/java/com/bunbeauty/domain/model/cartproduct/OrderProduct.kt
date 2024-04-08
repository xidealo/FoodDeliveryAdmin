package com.bunbeauty.domain.model.cartproduct

import android.os.Parcelable
import com.bunbeauty.domain.model.addition.OrderAddition
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderProduct(
    val uuid: String,
    val count: Int,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val newTotalCost: Int,
    val additionsPrice: Int?,
    val utils: String?,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val barcode: Int,
    val orderAdditions: List<OrderAddition>
) : Parcelable