package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull

class GetAdditionGroupListFromMenuProductUseCase(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(menuProductUuid: String): List<AdditionGroupWithAdditions> {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return menuProductRepo.getMenuProduct(
            menuProductUuid = menuProductUuid,
            companyUuid = companyUuid
        )?.additionGroups ?: emptyList()
    }
}
