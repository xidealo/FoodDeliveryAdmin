package com.bunbeauty.domain.model.cafe

import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType

data class UpdateCafe(
    val uuid: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val fromTime: Int? = null,
    val toTime: Int? = null,
    val offset: Int? = null,
    val phone: String? = null,
    val visible: Boolean? = null,
    val additionalUtensils: Boolean? = null,
    val cityUuid: String? = null,
    val workload: WorkLoad? = null,
    val workType: WorkType? = null
)
