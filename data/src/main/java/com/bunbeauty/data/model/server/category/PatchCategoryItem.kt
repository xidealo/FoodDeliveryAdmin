package com.bunbeauty.data.model.server.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatchCategoryList(
    @SerialName("patchCategoryItemList")
    val patchCategoryItemList: List<PatchCategoryItem>
)

@Serializable
data class PatchCategoryItem(
    @SerialName("name")
    val name: String,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("priority")
    val priority: Int
)
