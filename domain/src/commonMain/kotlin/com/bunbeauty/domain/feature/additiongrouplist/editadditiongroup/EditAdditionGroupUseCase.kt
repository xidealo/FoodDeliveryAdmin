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
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(
        additionGroupUuid: String,
        updateAdditionGroup: UpdateAdditionGroup,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val additionGroupList = additionGroupRepo.getAdditionGroupList(token = token)

        val old =
            additionGroupList.find { addition -> addition.uuid == additionGroupUuid }
                ?: throw NotFindAdditionGroupException()

        val isNameUnchanged = old.name == updateAdditionGroup.name
        val isVisibleUnchanged = old.isVisible == updateAdditionGroup.isVisible
        val isSingleChoiceUnchanged = old.singleChoice == updateAdditionGroup.singleChoice
        val name = updateAdditionGroup.name ?: throw AdditionGroupNameException()

        when {
            isNameUnchanged && isVisibleUnchanged && isSingleChoiceUnchanged -> return

            name.isBlank() -> throw AdditionGroupNameException()

            !isNameUnchanged && hasSameName(additionGroupList, updateAdditionGroup.name) ->
                throw DuplicateAdditionGroupNameException()

            else -> {
                additionGroupRepo.updateAdditionGroup(
                    updateAdditionGroup = updateAdditionGroup,
                    token = token,
                    additionGroupUuid = additionGroupUuid,
                )
            }
        }
    }

    private fun hasSameName(
        list: List<AdditionGroup>,
        name: String,
    ): Boolean = list.any { additionName -> additionName.name == name }
}
