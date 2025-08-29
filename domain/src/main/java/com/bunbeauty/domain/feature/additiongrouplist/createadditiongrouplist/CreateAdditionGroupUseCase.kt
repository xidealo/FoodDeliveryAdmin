package com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.CreateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class CreateAdditionGroupUseCase(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(additionName: String, isVisible: Boolean, singleChoice: Boolean) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val name = additionGroupRepo.getAdditionGroupList(
            token = token
        )

        when {
            additionName.isBlank() -> throw AdditionGroupNameException()
            getHasSameName(
                additionGroup = name,
                name = additionName
            ) -> throw DuplicateAdditionGroupNameException()
        }

        additionGroupRepo.postAdditionGroup(
            token = token,
            createAdditionGroup = CreateAdditionGroup(
                name = additionName,
                priority = 0,
                isVisible = isVisible,
                singleChoice = singleChoice
            )
        )
    }

    private fun getHasSameName(additionGroup: List<AdditionGroup>, name: String): Boolean {
        return additionGroup.any { additionGroup -> additionGroup.name == name }
    }
}
