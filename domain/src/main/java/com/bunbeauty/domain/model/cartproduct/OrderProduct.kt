package com.bunbeauty.domain.model.cartproduct

import com.bunbeauty.domain.model.addition.OrderAddition

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
)
