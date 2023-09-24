package com.bunbeauty.fooddeliveryadmin.screen.cafelist

import com.bunbeauty.fooddeliveryadmin.screen.cafelist.item.CafeUiItem

sealed interface CafeListUiState {
    data object Loading : CafeListUiState
    data object Error : CafeListUiState
    data class Success(val cafeItemList: List<CafeUiItem>) : CafeListUiState
}
