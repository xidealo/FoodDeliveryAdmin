package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.model.statistic.ProductStatistic
import com.bunbeauty.domain.model.statistic.Statistic
import javax.inject.Inject

class StatisticMapper @Inject constructor() : IStatisticMapper {

    override fun toModel(statisticServer: StatisticServer): Statistic {
        return Statistic(
            period = statisticServer.period,
            startPeriodTime = statisticServer.startPeriodTime,
            orderCount = statisticServer.orderCount,
            proceeds = statisticServer.proceeds,
            averageCheck = statisticServer.averageCheck,
            productStatisticList = statisticServer.productStatisticList.map {
                ProductStatistic(
                    name = it.name,
                    orderCount = it.orderCount,
                    productCount = it.productCount,
                    proceeds = it.proceeds,
                )
            }
        )
    }

}