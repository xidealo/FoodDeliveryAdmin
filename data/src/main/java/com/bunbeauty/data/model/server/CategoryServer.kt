package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class CategoryServer(
    val uuid: String = "",
    val name: String = "",
    val priority: Int = 0
)
