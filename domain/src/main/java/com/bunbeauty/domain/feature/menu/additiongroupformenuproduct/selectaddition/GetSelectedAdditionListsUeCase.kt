package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.MenuProductRepo

class GetSelectedAdditionListsUeCase(
    private val menuProductRepo: MenuProductRepo
) {
    suspend operator fun invoke(
        menuProductUuid: String,
        additionGroupUuid: String
    ) {

    }
}