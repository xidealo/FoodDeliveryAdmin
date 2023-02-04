package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class MenuProductServer(
    val uuid: String? = null,
    val name: String? = null,
    val newPrice: Int? = null,
    val oldPrice: Int? = null,
    val utils: String? = null,
    val nutrition: Int? = null,
    val description: String? = null,
    val comboDescription: String? = null,
    val photoLink: String? = null,
    val barcode: Int? = null,
    val isVisible: Boolean? = null,
    val categories: List<CategoryServer>? = null
)
