package com.bunbeauty.fooddeliveryadmin.screen.menu.domain

import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class SeparatedMenuProductList(
    val visibleList: List<MenuProduct>,
    val hiddenList: List<MenuProduct>
)

class GetSeparatedMenuProductListUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(isRefreshing: Boolean): List<MenuProduct> {
        return menuProductRepo.getMenuProductList(
            companyUuid = dataStoreRepo.companyUuid.first(),
            isRefreshing = isRefreshing
        ).sortedBy { it.name }
    }
}
