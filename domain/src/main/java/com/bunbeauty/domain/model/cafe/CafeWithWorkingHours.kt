package com.bunbeauty.domain.model.cafe

data class CafeWithWorkingHours(
    val uuid: String,
    val address: String,
    val workingHours: String,
    val status: CafeStatus,
    val cityUuid: String
) {
    companion object {
        val mock = CafeWithWorkingHours(
            uuid = "",
            address = "",
            workingHours = "",
            status = CafeStatus.Closed,
            cityUuid = ""
        )
    }
}
