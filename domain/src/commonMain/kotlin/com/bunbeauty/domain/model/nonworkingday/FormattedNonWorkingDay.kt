package com.bunbeauty.domain.model.nonworkingday

data class FormattedNonWorkingDay(
    val uuid: String,
    val date: String,
    val cafeUuid: String,
) {
    companion object {
        val mock =
            FormattedNonWorkingDay(
                uuid = "",
                date = "",
                cafeUuid = "",
            )
    }
}
