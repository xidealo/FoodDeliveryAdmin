package com.bunbeauty.data.model.server.statistic

import kotlinx.serialization.Serializable

@Serializable
data class StatisticServer(
    val period: String,
    val startPeriodTime: Long,
    val orderCount: Int,
    val proceeds: Int,
    val averageCheck: Int
)