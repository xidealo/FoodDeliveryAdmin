package com.bunbeauty.domain.model.nonworkingday

data class NonWorkingDay(
    val uuid: String,
    val timestamp: Long,
    val cafeUuid: String,
    val isVisible: Boolean,
) {
    companion object {
        val mock =
            NonWorkingDay(
                uuid = "",
                timestamp = 0,
                cafeUuid = "",
                isVisible = false,
            )
    }
}
