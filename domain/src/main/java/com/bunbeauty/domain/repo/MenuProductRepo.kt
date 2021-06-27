package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.ServerMenuProduct
import kotlinx.coroutines.flow.Flow

interface MenuProductRepo {

    val menuProductList: Flow<List<MenuProduct>>
}
