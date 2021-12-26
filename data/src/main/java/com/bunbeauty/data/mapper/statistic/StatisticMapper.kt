package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.domain.model.statistic.Statistic
import javax.inject.Inject

class StatisticMapper @Inject constructor(): IStatisticMapper {

    override fun toModel(statisticServer: StatisticServer): Statistic {
        return Statistic(
            period = "",
            orderList = emptyList()
        )
    }

}