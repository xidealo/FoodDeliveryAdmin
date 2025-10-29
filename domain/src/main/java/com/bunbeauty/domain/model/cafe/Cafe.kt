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
    val additional: Boolean,
    val cityUuid: String,
    val workload: WorkLoad,
    val workType: WorkType
) {
    companion object {
        val mock = Cafe(
            uuid = "",
            address = "",
            latitude = 0.0,
            longitude = 0.0,
            fromTime = 0,
            toTime = 0,
            offset = 0,
            phone = "",
            visible = false,
            additional = false,
            cityUuid = "",
            workload = WorkLoad.LOW,
            workType = WorkType.PICKUP
        )
    }
}
