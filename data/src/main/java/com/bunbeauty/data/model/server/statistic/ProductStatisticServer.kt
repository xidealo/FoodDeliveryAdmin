package com.bunbeauty.data.model.server.statistic

import kotlinx.serialization.Serializable

@Serializable
data class ProductStatisticServer(
    val name: String,
    val orderCount: Int,
    val productCount: Int,
    val proceeds: Int,
)