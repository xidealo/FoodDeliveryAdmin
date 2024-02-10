package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct

interface MenuProductRepo {
    /*GET*/
    suspend fun getMenuProductList(
        companyUuid: String,
        takeRemote: Boolean = true
    ): List<MenuProduct>?

    suspend fun getMenuProduct(menuProductUuid: String): MenuProduct?

    /*UPDATE*/
    suspend fun updateMenuProduct(
        menuProductUuid: String,
        updateMenuProduct: UpdateMenuProduct,
        token: String
    )

    /*DELETE*/
    suspend fun clearCache()

    /*OTHER*/
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String
}
