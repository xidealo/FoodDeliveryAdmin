package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menu_product.MenuProduct
import kotlinx.coroutines.flow.Flow

interface MenuProductRepo {

    val menuProductList: Flow<List<MenuProduct>>
}
