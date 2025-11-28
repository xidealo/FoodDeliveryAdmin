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
    val orderAdditions: List<OrderAddition>,
) {
    companion object {
        val mock =
            OrderProduct(
                uuid = "",
                count = 0,
                name = "",
                newPrice = 0,
                oldPrice = null,
                newTotalCost = 0,
                additionsPrice = null,
                utils = null,
                nutrition = null,
                description = "",
                comboDescription = null,
                barcode = 0,
                orderAdditions = emptyList(),
            )
    }
}
