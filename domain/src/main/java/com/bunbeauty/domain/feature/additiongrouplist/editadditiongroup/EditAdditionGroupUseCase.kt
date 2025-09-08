package com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.AdditionGroupNameException
import com.bunbeauty.domain.feature.additiongrouplist.createadditiongrouplist.DuplicateAdditionGroupNameException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class EditAdditionGroupUseCase(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        additionGroupUuid: String,
        updateAdditionGroup: UpdateAdditionGroup
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val additionGroupList = additionGroupRepo.getAdditionGroupList(token = token)

        val oldAdditionGroup = additionGroupList.find { addition -> addition.uuid == additionGroupUuid }
            ?: throw NotFindAdditionGroupException()

        when {
            updateAdditionGroup.name.isBlank() -> throw AdditionGroupNameException()
            isNameUnchanged(oldName = oldAdditionGroup.name, newName = updateAdditionGroup.name) -> return
            getHasSameName(
                additionGroupList = additionGroupList,
                name = updateAdditionGroup.name
            ) -> throw DuplicateAdditionGroupNameException()
        }

        additionGroupRepo.updateAdditionGroup(
            updateAdditionGroup = updateAdditionGroup,
            token = token,
            additionGroupUuid = additionGroupUuid
        )
    }

    private fun getHasSameName(additionGroupList: List<AdditionGroup>, name: String): Boolean {
        return additionGroupList.any { additionName -> additionName.name == name }
    }

    private fun isNameUnchanged(oldName: String, newName: String): Boolean {
        return oldName == newName
    }
}
