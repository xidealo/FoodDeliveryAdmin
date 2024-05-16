package com.bunbeauty.presentation.feature.menulist.editmenuproduct

sealed interface EditMenuProductEvent {
    data class ShowUpdateProductSuccess(val productName: String) : EditMenuProductEvent
    data class ShowUpdateProductError(val productName: String) : EditMenuProductEvent
}
