package com.bunbeauty.data.model.server.menu_product

import com.bunbeauty.data.model.server.CategoryServer
import kotlinx.serialization.Serializable

@Serializable
data class MenuProductServer(
    val uuid: String,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val utils: String?,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int,
    val isVisible: Boolean,
    val categories: List<CategoryServer>
)
