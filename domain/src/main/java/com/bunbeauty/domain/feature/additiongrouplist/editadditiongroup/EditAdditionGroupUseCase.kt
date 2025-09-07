package com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.category.CategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.DuplicateCategoryNameException
import com.bunbeauty.domain.feature.menu.common.category.NotFindCategoryException
import com.bunbeauty.domain.feature.menu.common.model.Category
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
    ){
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val additionGroupList = additionGroupRepo.getAdditionGroupList(token = token)

        val oldAdditionGroup = additionGroupList.find { addition -> addition.uuid == additionGroupUuid }
            ?: throw NotFindCategoryException() // добавляем

        when {
            updateAdditionGroup.name.isBlank() -> throw CategoryNameException()
            isNameUnchanged(oldName = oldAdditionGroup.name, newName = updateAdditionGroup.name) -> return
            getHasSameName(
                additionGroupList = additionGroupList,
                name = updateAdditionGroup.name
            ) -> throw DuplicateCategoryNameException()  // добавяем
        }
    }

    private fun getHasSameName(additionGroupList: List<AdditionGroup>, name: String): Boolean {
        return additionGroupList.any { additionName -> additionName.name == name }
    }

    private fun isNameUnchanged(oldName: String, newName: String): Boolean {
        return oldName == newName
    }
}