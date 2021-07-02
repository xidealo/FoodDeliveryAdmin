package com.bunbeauty.domain.model.menu_product

import com.bunbeauty.domain.model.ListModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProductCode(
    override val title: String
): ListModel()
