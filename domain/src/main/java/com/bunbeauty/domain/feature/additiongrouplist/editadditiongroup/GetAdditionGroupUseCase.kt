package com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup


import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class GetAdditionGroupUseCase(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(additionGroupUuid: String): AdditionGroup {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        return additionGroupRepo.getAdditionGroup(
            additionUuid = additionGroupUuid,
            token = token
        ) ?: throw NotFoundAdditionGroupException()
    }
}