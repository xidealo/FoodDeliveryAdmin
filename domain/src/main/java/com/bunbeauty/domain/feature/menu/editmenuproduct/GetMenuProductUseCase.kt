package com.bunbeauty.domain.feature.menu.editmenuproduct

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.NotFoundMenuProductException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull

class GetMenuProductUseCase (
    private val dataStoreRepo: DataStoreRepo,
    private val menuProductRepo: MenuProductRepo
) {
    suspend operator fun invoke(menuProductUuid: String): MenuProduct {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return menuProductRepo.getMenuProduct(
            menuProductUuid = menuProductUuid,
            companyUuid = companyUuid
        ) ?: throw NotFoundMenuProductException()
    }
}
