package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.domain.model.menu_product.MenuProduct

sealed class MenuNavigationEvent: NavigationEvent() {
    object ToCreateMenuProduct: MenuNavigationEvent()
    data class ToEditMenuProduct(val menuProduct: MenuProduct): MenuNavigationEvent()
}
