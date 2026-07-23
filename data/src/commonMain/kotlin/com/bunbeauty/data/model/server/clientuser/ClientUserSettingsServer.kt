package com.bunbeauty.data.model.server.clientuser

import kotlinx.serialization.Serializable

@Serializable
data class ClientUserSettingsServer(
    val uuid: String,
    val phoneNumber: String,
    val email: String?,
    val isActive: Boolean,
)
