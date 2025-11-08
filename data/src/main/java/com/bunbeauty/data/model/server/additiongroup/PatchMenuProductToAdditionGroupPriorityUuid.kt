package com.bunbeauty.data.model.server.additiongroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatchMenuProductToAdditionGroupPriorityUuid(
    @SerialName("additionGroupUuidList")
    val additionGroupUuidList: List<String>
) {
    companion object {
        val mock = PatchMenuProductToAdditionGroupPriorityUuid(
            additionGroupUuidList = emptyList()
        )
    }
}
