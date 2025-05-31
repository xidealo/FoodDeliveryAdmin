package com.bunbeauty.data.model.server.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryServer(
    val uuid: String = "",
    val name: String = "",
    val priority: Int = 0
)


@Serializable
data class CategoryPriorityServer(
    val uuid: String,
    val name: String,
    val priority: Int
)
