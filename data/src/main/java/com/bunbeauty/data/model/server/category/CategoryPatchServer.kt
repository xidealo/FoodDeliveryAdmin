package com.bunbeauty.data.model.server.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryPatchServer(
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int
)
