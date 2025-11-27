package com.bunbeauty.data.model.server.menuproduct

import kotlinx.serialization.Serializable

@Serializable
data class MenuProductAdditionsPatchServer(
    val additionGroupUuid: String?,
    val additionUuidList: List<String>?,
)
