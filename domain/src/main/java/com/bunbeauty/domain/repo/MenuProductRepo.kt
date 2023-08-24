package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menuproduct.MenuProduct

interface MenuProductRepo {
    /*GET*/
    suspend fun getMenuProductList(
        companyUuid: String,
        takeRemote: Boolean = true
    ): List<MenuProduct>

    suspend fun getMenuProduct(menuProductUuid: String): MenuProduct?

    /*UPDATE*/
    suspend fun updateMenuProduct(
        menuProduct: MenuProduct,
        token: String
    )

    suspend fun updateVisibleMenuProductUseCase(uuid: String, isVisible: Boolean, token: String)

    /*DELETE*/
    suspend fun deleteMenuProduct(uuid: String)
    suspend fun clearCache()

    /*OTHER*/
    suspend fun deleteMenuProductPhoto(photoLink: String)
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String
}
