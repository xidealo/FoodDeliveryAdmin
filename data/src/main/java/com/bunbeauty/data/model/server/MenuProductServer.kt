package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class MenuProductServer(
    val uuid: String = "",
    val name: String = "",
    val newPrice: Int = 0,
    val oldPrice: Int = 0,
    val utils: String = "",
    val nutrition: Int = 0,
    val description: String = "",
    val comboDescription: String? = null,
    val photoLink: String = "",
    val barcode: Int = 0,
    val isVisible: Boolean = false,
    val categories: List<CategoryServer> = emptyList()
)
