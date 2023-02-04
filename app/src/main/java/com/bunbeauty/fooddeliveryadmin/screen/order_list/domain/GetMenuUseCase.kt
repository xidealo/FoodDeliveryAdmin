package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMenuUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(): List<MenuProduct> {
        return menuProductRepo.getMenuProductList(
            companyUuid = dataStoreRepo.companyUuid.first()
        )
    }
}