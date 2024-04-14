package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroup

interface AdditionGroupRepo {
    /*GET*/
    suspend fun getAdditionGroupList(
        token: String,
        takeRemote: Boolean = true
    ): List<AdditionGroup>

    suspend fun getAdditionGroup(additionUuid: String): AdditionGroup?

    /*UPDATE*/
    suspend fun updateAdditionGroup(
        token: String,
        addition: AdditionGroup,
    )

    /*DELETE*/
    suspend fun clearCache()
}
