package com.bunbeauty.domain.repository.menu_product

import androidx.lifecycle.LiveData
import com.bunbeauty.data.enums.ProductCode
import com.bunbeauty.data.model.MenuProduct

interface MenuProductRepo {

    suspend fun insert(menuProduct: MenuProduct)

    suspend fun getMenuProductRequest()
    fun getMenuProductList(productCode: ProductCode): LiveData<List<MenuProduct>>
}