package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.domain.model.statistic.Statistic

class StatisticMapper : IStatisticMapper {

    override fun toModel(statisticServer: StatisticServer): Statistic {
        return Statistic(
            period = "",
            orderList = emptyList()
        )
    }

}