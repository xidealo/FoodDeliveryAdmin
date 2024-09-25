package com.bunbeauty.presentation.feature.cafelist

import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours

data class CafeListDataState(
    val state: State,
    val cafeList: List<CafeWithWorkingHours>? = null
) {

    enum class State {
        LOADING,
        ERORR,
        SUCCESS
    }
}
