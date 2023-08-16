package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class SeparatedMenuProductList(
    val visibleList: List<MenuProduct>,
    val hiddenList: List<MenuProduct>
)

class GetSeparatedMenuProductListUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(isRefreshing: Boolean): SeparatedMenuProductList {
        val menuProductList = menuProductRepo.getMenuProductList(
            companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException(),
            isRefreshing = isRefreshing
        )

        return SeparatedMenuProductList(
            visibleList = menuProductList
                .filter { it.isVisible }
                .sortedBy { it.name },
            hiddenList = menuProductList
                .filterNot { it.isVisible }
                .sortedBy { it.name },
        )
    }
}
