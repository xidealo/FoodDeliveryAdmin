package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menu_product.MenuProduct

interface MenuProductRepo {
    suspend fun getMenuProductList(
        companyUuid: String,
        loadFromServer: Boolean = true
    ): List<MenuProduct>

    suspend fun getMenuProductList(): List<MenuProduct>

    suspend fun deleteMenuProductPhoto(photoLink: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String
    suspend fun saveMenuProduct(menuProduct: MenuProduct)
    suspend fun updateVisibleMenuProductUseCase(uuid: String, isVisible: Boolean, token: String)
    suspend fun deleteMenuProduct(uuid: String)
}
