package com.bunbeauty.domain.model.nonworkingday

data class NewNonWorkingDay(
    val timestamp: Long,
    val cafeUuid: String
) {
    companion object {
        val mock = NewNonWorkingDay(
            timestamp = 0,
            cafeUuid = ""
        )
    }
}
