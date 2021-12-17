package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorization(
    val username: String = "",
    val password: String = "",
    val token: String = ""
)
