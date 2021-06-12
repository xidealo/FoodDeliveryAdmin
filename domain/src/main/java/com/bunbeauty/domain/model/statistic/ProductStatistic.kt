package com.bunbeauty.domain.model.statistic

data class ProductStatistic(
    val name: String,
    val photoLink: String,
    var orderCount: Int,
    var count: Int,
    var cost: Int
)
