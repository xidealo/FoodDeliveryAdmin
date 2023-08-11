package com.bunbeauty.presentation.feature.cafe_list

import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours

data class CafeListUiState(
    val state: State
) {

    sealed interface State {
        object Loading: State
        object Error: State
        data class Success(val cafeList: List<CafeWithWorkingHours>): State
    }
}