package com.bunbeauty.domain.model.cafe

data class CafeWithWorkingHours(
    val uuid: String,
    val address: String,
    val workingHours: String,
    val status: CafeStatus,
    val cityUuid: String
)