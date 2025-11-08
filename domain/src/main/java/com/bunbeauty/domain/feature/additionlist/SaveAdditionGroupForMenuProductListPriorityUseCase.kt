package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository

class SaveAdditionGroupForMenuProductListPriorityUseCase(
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(additionGroupList: List<String>) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        menuProductToAdditionGroupRepository.saveMenuProductToAdditionGroupPriorityUuid(
            token = token,
            additionGroupListUuid = additionGroupList
        )
    }
}
