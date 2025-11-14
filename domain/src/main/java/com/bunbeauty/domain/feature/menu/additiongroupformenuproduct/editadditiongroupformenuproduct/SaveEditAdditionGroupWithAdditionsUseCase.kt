package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.domain.repo.MenuProductRepo

class SaveEditAdditionGroupWithAdditionsUseCase(
    private val menuProductRepo: MenuProductRepo,
) {
    suspend operator fun invoke(
        menuProductToAdditionGroupUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>?,
    ) {
        menuProductRepo.updateMenuProductAdditions(
            menuProductToAdditionGroupUuid = menuProductToAdditionGroupUuid,
            additionGroupUuid = additionGroupUuid,
            additionList = additionList,
        )
    }
}
