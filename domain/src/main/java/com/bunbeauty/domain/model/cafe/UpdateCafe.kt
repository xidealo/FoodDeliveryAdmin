package com.bunbeauty.domain.model.cafe

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
    val cityUuid: String? = null,
    val workload: String? = null,
    val workType: String? = null
)
