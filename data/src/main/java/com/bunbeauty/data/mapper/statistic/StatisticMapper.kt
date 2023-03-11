package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.model.statistic.ProductStatistic
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import javax.inject.Inject

class StatisticMapper @Inject constructor(
    private val dateTimeUtil: DateTimeUtil
    ) {

    fun toModel(statisticServer: StatisticServer): Statistic {
        return Statistic(
            period = dateTimeUtil.getDateTimeDDMMHHMM(statisticServer.startPeriodTime),
            startPeriodTime = statisticServer.startPeriodTime,
            orderCount = statisticServer.orderCount,
            proceeds = statisticServer.proceeds,
        )
    }

}