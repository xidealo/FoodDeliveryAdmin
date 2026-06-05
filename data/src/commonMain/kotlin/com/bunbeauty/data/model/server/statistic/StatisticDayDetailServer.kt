package com.bunbeauty.data.model.server.statistic

import kotlinx.serialization.Serializable

@Serializable
data class StatisticDayDetailServer(
    val companyUuid: String,
    val date: String,
    val periodType: String? = null,
    val periodStart: String? = null,
    val periodEnd: String? = null,
    val orderCount: Int,
    val orderProceedsTotal: Int,
    val orderProceedsProducts: Int,
    val averageCheck: Double,
    val deliveryOrderCount: Int,
    val pickupOrderCount: Int,
    val products: List<StatisticDayProductServer>,
)

@Serializable
data class StatisticDayProductServer(
    val menuProductUuid: String,
    val name: String,
    val photoLink: String,
    val productCount: Int,
    val proceeds: Int,
)
