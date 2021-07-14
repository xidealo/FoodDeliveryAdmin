package com.bunbeauty.presentation.model

import com.bunbeauty.domain.model.statistic.Statistic

data class StatisticItemModel(
    var period: String,
    var count: String,
    var proceeds: String,
    var statistic: Statistic
)
