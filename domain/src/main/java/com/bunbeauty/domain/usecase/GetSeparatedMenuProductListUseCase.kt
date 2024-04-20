package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class SeparatedMenuProductList(
    val visibleList: List<MenuProduct>,
    val hiddenList: List<MenuProduct>
)

class GetSeparatedMenuProductListUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(takeRemote: Boolean): SeparatedMenuProductList {
        val menuProductList = menuProductRepo.getMenuProductList(
            companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException(),
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
