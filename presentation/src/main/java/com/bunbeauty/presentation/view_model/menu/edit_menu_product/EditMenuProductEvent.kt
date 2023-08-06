package com.bunbeauty.presentation.view_model.menu.edit_menu_product

sealed interface EditMenuProductEvent {
    data class ShowUpdatProductSuccess(val productName: String) : EditMenuProductEvent
    data class ShowUpdateProductError(val productName: String) : EditMenuProductEvent
}
