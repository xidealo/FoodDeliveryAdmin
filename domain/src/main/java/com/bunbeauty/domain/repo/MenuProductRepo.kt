package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.MenuProduct
import kotlinx.coroutines.flow.Flow

interface MenuProductRepo {

    suspend fun insert(menuProduct: MenuProduct)
    suspend fun updateRequest(menuProduct: MenuProduct)

    suspend fun refreshProductList()
    fun getMenuProductList(): Flow<List<MenuProduct>>
}
