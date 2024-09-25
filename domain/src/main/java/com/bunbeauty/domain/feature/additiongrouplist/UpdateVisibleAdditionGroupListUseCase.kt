package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class UpdateVisibleAdditionGroupListUseCase @Inject constructor(
    private val additionGroupRepo: AdditionGroupRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(additionUuidGroup: String, isVisible: Boolean) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        additionGroupRepo.updateAdditionGroup(
            additionGroupUuid = additionUuidGroup,
            updateAdditionGroup = UpdateAdditionGroup(isVisible = isVisible),
            token = token
        )
    }
}
