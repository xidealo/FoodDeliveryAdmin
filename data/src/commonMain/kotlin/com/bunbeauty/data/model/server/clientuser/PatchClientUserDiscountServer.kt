package com.bunbeauty.data.model.server.clientuser

import kotlinx.serialization.Serializable

@Serializable
data class PatchClientUserDiscountServer(
    val percentDiscount: Int,
)
