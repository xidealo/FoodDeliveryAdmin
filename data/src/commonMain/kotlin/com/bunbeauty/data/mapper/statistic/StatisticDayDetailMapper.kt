package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.statistic.StatisticDayDetailServer
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDayProduct
import common.Constants.RUBLE_CURRENCY

class StatisticDayDetailMapper {
    fun toModel(server: StatisticDayDetailServer): StatisticDayDetail =
        StatisticDayDetail(
            companyUuid = server.companyUuid,
            date = server.date,
            orderCount = server.orderCount,
            orderProceedsTotal = server.orderProceedsTotal,
            orderProceedsProducts = server.orderProceedsProducts,
            averageCheck = server.averageCheck,
            deliveryOrderCount = server.deliveryOrderCount,
            pickupOrderCount = server.pickupOrderCount,
            products =
                server.products.map { product ->
                    StatisticDayProduct(
                        menuProductUuid = product.menuProductUuid,
                        name = product.name,
                        photoLink = product.photoLink,
                        productCount = product.productCount,
                        proceeds = product.proceeds,
                        currency = RUBLE_CURRENCY,
                    )
                },
            currency = RUBLE_CURRENCY,
        )
}
