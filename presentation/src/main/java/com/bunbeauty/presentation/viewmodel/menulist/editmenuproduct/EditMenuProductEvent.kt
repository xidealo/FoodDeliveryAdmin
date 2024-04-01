package com.bunbeauty.presentation.viewmodel.menulist.editmenuproduct

sealed interface EditMenuProductEvent {
    data class ShowUpdateProductSuccess(val productName: String) : EditMenuProductEvent
    data class ShowUpdateProductError(val productName: String) : EditMenuProductEvent
}
