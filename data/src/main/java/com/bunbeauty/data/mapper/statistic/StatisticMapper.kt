package com.bunbeauty.data.mapper.statistic

import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.data.model.server.statistic.StatisticServer
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM

class StatisticMapper(
    private val dateTimeUtil: DateTimeUtil
) {

    fun toModel(statisticServer: StatisticServer): Statistic {
        return Statistic(
            period = dateTimeUtil.formatDateTime(
                statisticServer.startPeriodTime,
                PATTERN_DD_MMMM_HH_MM
            ),
            startPeriodTime = statisticServer.startPeriodTime,
            orderCount = statisticServer.orderCount,
            proceeds = statisticServer.proceeds,
            currency = RUBLE_CURRENCY
        )
    }
}
