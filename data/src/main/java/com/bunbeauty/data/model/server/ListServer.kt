package com.bunbeauty.data.model.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListServer<T>(

    @SerialName("count")
    val count: Int,

    @SerialName("results")
    val results: List<T>
)