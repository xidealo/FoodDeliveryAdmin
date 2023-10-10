package com.bunbeauty.data.model.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartProductServer(
    val uuid: String = "",
    val count: Int = 0,
    val name: String = "",
    val newPrice: Int = 0,
    val oldPrice: Int? = null,
    val utils: String? = null,
    val nutrition: Int? = null,
    val description: String = "",
    val comboDescription: String? = null,
    @SerialName("newTotalCost")
    val newTotalCost: Int,
    @SerialName("additionsPrice")
    val additionsPrice: Int?,
    val barcode: Int = 0,
    @SerialName("additions")
    val additions: List<AdditionServer>
)
