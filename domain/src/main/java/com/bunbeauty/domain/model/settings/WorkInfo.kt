package com.bunbeauty.domain.model.settings

data class WorkInfo(
    val workType: WorkType,
) {
    enum class WorkType {
        DELIVERY,
        PICKUP,
        DELIVERY_AND_PICKUP,
        CLOSED,
    }

    companion object {
        val mock =
            WorkInfo(
                workType = WorkType.CLOSED,
            )
    }
}
