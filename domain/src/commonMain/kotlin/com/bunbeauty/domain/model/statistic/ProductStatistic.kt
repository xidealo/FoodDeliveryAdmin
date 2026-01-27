package com.bunbeauty.domain.model.statistic

data class ProductStatistic(
    val name: String,
    var orderCount: Int,
    var productCount: Int,
    var proceeds: Int,
)
