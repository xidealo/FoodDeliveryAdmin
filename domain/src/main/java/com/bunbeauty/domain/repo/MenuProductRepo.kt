package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct

interface MenuProductRepo {
    suspend fun saveMenuProduct(
        token: String,
        menuProductPost: MenuProductPost,
    ): MenuProduct?

    // GET
    suspend fun getMenuProductList(
        companyUuid: String,
        takeRemote: Boolean = true,
    ): List<MenuProduct>?

    suspend fun getMenuProduct(
        menuProductUuid: String,
        companyUuid: String,
    ): MenuProduct?

    // UPDATE
    suspend fun updateMenuProduct(
        menuProductUuid: String,
        updateMenuProduct: UpdateMenuProduct,
        token: String,
    ): MenuProduct?

    // UPDATE
    suspend fun updateMenuProductAdditions(
        menuProductToAdditionGroupUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>?,
    )

    // DELETE
    fun clearCache()

    // OTHER
    suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String
}
