package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menu_product.MenuProduct
import kotlinx.coroutines.flow.Flow

interface MenuProductRepo {
    suspend fun refreshMenuProductList()
    fun getMenuProductList(): Flow<List<MenuProduct>>

    suspend fun deleteMenuProductPhoto(photoLink: String)
    fun saveMenuProductPhoto(photoByteArray: ByteArray): String
    suspend fun saveMenuProduct(menuProduct: MenuProduct)
    suspend fun updateMenuProduct(menuProduct: MenuProduct)
    suspend fun deleteMenuProduct(uuid: String)
}
