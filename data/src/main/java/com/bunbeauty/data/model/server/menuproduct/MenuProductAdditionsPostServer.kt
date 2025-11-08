package com.bunbeauty.data.model.server.menuproduct

import kotlinx.serialization.Serializable

@Serializable
data class MenuProductAdditionsPostServer(
    val menuProductUuids: List<String>,
    val additionGroupUuid: String,
    val additionUuids: List<String>
)
