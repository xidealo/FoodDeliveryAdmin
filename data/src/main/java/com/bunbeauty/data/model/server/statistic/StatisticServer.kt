package com.bunbeauty.data.model.server.statistic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("periodType")
    val period: String,
    @SerialName("time")
    val startPeriodTime: Long,
    @SerialName("orderCount")
    val orderCount: Int,
    @SerialName("orderProceeds")
    val proceeds: Int,
)
