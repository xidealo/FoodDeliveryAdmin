package com.bunbeauty.data.model.server.menuproduct

import kotlinx.serialization.Serializable

@Serializable
data class MenuProductPatchServer(
    val name: String?,
    val newPrice: Int?,
    val oldPrice: Int?,
    val utils: String?,
    val nutrition: Int?,
    val description: String?,
    val comboDescription: String?,
    val photoLink: String?,
    val categoryUuids: List<String>?,
    val isVisible: Boolean?,
    val isRecommended: Boolean?
)
