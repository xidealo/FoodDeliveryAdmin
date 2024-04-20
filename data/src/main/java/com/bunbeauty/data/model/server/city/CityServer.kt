package com.bunbeauty.data.model.server.city

import kotlinx.serialization.Serializable

@Serializable
data class CityServer(
    val uuid: String,
    val name: String,
    val timeZone: String,
    val isVisible: Boolean
)
