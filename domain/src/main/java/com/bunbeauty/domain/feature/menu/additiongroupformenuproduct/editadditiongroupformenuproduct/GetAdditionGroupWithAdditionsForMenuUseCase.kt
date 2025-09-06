package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.firstOrNull

//todo tests
class GetAdditionGroupWithAdditionsForMenuUseCase(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        menuProductUuid: String,
        additionGroupForMenuUuid: String
    ): AdditionGroupWithAdditions {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()

        return menuProductRepo.getMenuProduct(
            companyUuid = companyUuid,
            menuProductUuid = menuProductUuid
        )?.additionGroups
            ?.find { additionGroupWithAdditions ->
                additionGroupWithAdditions.additionGroup.uuid == additionGroupForMenuUuid
            }
            ?: throw NoAdditionGroupException()
    }
}