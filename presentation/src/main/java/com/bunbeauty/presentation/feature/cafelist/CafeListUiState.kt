package com.bunbeauty.presentation.feature.cafelist

import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours

data class CafeListUiState(
    val state: State
) {

    sealed interface State {
        data object Loading: State
        data object Error: State
        data class Success(val cafeList: List<CafeWithWorkingHours>): State
    }
}