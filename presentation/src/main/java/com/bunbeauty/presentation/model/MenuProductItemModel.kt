package com.bunbeauty.presentation.model

import com.bunbeauty.domain.model.menu_product.MenuProduct

data class MenuProductItemModel(
    val name: String,
    val photoLink: String,
    val visible: Boolean,
    val newCost: String,
    val oldCost: String,
    val menuProduct: MenuProduct
)
