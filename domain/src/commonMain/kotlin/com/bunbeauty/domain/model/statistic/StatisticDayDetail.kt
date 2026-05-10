package com.bunbeauty.domain.model.statistic

data class StatisticDayDetail(
    val companyUuid: String,
    val date: String,
    val orderCount: Int,
    val orderProceedsTotal: Int,
    val orderProceedsProducts: Int,
    val averageCheck: Double,
    val deliveryOrderCount: Int,
    val pickupOrderCount: Int,
    val products: List<StatisticDayProduct>,
    val currency: String,
)

data class StatisticDayProduct(
    val menuProductUuid: String,
    val name: String,
    val photoLink: String,
    val productCount: Int,
    val proceeds: Int,
    val currency: String,
)
