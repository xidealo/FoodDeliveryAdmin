package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.createadditiongroupformenuproduct

import com.bunbeauty.domain.exception.NoAdditionGroupException
import com.bunbeauty.domain.exception.NoAdditionListException
import com.bunbeauty.domain.repo.MenuProductRepo

class CreateEditAdditionGroupWithAdditionsUseCase(
    private val menuProductRepo: MenuProductRepo,
) {
    suspend operator fun invoke(
        menuProductUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>,
    ) {
        if (additionGroupUuid == null) {
            throw NoAdditionGroupException()
        }

        if (additionList.isEmpty()) {
            throw NoAdditionListException()
        }

        menuProductRepo.createMenuProductAdditions(
            menuProductUuid = menuProductUuid,
            additionGroupUuid = additionGroupUuid,
            additionList = additionList,
        )
    }
}
