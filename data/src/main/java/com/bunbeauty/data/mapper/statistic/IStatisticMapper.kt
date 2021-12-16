package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.StatisticServer
import com.bunbeauty.domain.model.statistic.Statistic

interface IStatisticMapper {
    fun toModel(statisticServer: StatisticServer): Statistic
}