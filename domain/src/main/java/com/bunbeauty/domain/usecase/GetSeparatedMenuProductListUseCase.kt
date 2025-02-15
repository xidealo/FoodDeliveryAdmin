package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull

data class SeparatedMenuProductList(
    val visibleList: List<MenuProduct>,
    val hiddenList: List<MenuProduct>
)

class GetSeparatedMenuProductListUseCase(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(takeRemote: Boolean): SeparatedMenuProductList {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        val menuProductList = menuProductRepo.getMenuProductList(
            companyUuid = companyUuid,
            takeRemote = takeRemote
        ).orEmpty()

        return SeparatedMenuProductList(
            visibleList = menuProductList
                .filter { menuProduct ->
                    menuProduct.isVisible
                }
                .sortedBy { menuProduct ->
                    menuProduct.name
                },
            hiddenList = menuProductList
                .filterNot { menuProduct ->
                    menuProduct.isVisible
                }
                .sortedBy { menuProduct ->
                    menuProduct.name
                }
        )
    }
}
