package com.bunbeauty.data.model.server.menuproduct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuProductPostServer(
    @SerialName("name")
    val name: String,
    @SerialName("newPrice")
    val newPrice: Int,
    @SerialName("oldPrice")
    val oldPrice: Int?,
    @SerialName("utils")
    val utils: String?,
    @SerialName("nutrition")
    val nutrition: Int?,
    @SerialName("description")
    val description: String,
    @SerialName("comboDescription")
    val comboDescription: String?,
    @SerialName("photoLink")
    val photoLink: String,
    @SerialName("barcode")
    val barcode: Int,
    @SerialName("isVisible")
    val isVisible: Boolean,
    @SerialName("isRecommended")
    val isRecommended: Boolean,
    @SerialName("categoryUuids")
    val categories: List<String>
)
