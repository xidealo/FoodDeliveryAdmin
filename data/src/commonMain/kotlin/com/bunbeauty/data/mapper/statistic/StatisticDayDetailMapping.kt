package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.statistic.StatisticDayDetailServer
import com.bunbeauty.data.model.server.statistic.StatisticDayProductServer
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDayProduct
import com.bunbeauty.domain.model.statistic.StatisticDetailPeriod
import common.Constants.RUBLE_CURRENCY

internal fun StatisticDayDetailServer.toDomain(): StatisticDayDetail {
    val resolvedPeriod =
        periodType?.let { raw ->
            StatisticDetailPeriod.entries.firstOrNull { entry ->
                entry.name == raw
            }
        } ?: StatisticDetailPeriod.DAY
    val resolvedStart = periodStart ?: date
    val resolvedEnd = periodEnd ?: date

    return StatisticDayDetail(
        companyUuid = companyUuid,
        date = date,
        periodType = resolvedPeriod,
        periodStart = resolvedStart,
        periodEnd = resolvedEnd,
        orderCount = orderCount,
        orderProceedsTotal = orderProceedsTotal,
        orderProceedsProducts = orderProceedsProducts,
        averageCheck = averageCheck,
        deliveryOrderCount = deliveryOrderCount,
        pickupOrderCount = pickupOrderCount,
        products = products.map { product -> product.toDomain() },
        currency = RUBLE_CURRENCY,
    )
}

private fun StatisticDayProductServer.toDomain(): StatisticDayProduct =
    StatisticDayProduct(
        menuProductUuid = menuProductUuid,
        name = name,
        photoLink = photoLink,
        productCount = productCount,
        proceeds = proceeds,
        currency = RUBLE_CURRENCY,
    )
