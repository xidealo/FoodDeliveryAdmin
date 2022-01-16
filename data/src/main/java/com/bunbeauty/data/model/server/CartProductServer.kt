package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class CartProductServer(
    val uuid: String = "",
    val count: Int = 0,
    val name: String = "",
    val newPrice: Int = 0,
    val oldPrice: Int? = null,
    val utils: String = "",
    val nutrition: Int = 0,
    val description: String = "",
    val comboDescription: String? = null,
    val barcode: Int = 0,
)
