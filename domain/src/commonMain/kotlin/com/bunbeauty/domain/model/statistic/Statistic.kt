package com.bunbeauty.domain.model.statistic

data class Statistic(
    val period: String,
    val startPeriodTime: Long,
    val orderCount: Int,
    val proceeds: Int,
    val currency: String,
)
