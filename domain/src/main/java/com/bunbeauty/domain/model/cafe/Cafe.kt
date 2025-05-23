package com.bunbeauty.domain.model.cafe

import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType

data class Cafe(
    val uuid: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val fromTime: Int,
    val toTime: Int,
    val offset: Int,
    val phone: String,
    val visible: Boolean,
    val cityUuid: String,
    val workload: WorkLoad,
    val workType: WorkType
)
