package com.bunbeauty.presentation.model

import com.bunbeauty.domain.model.menu_product.MenuProduct

data class MenuViewState(
    val menuProductItems: List<MenuProductItem> = listOf(),
    val isLoading: Boolean = true
) {

    data class MenuProductItem(
        val name: String,
        val photoLink: String,
        val visible: Boolean,
        val newCost: String,
        val oldCost: String,
    )

}
