package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.CreateAdditionGroup
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup

interface AdditionGroupRepo {
    /*GET*/
    suspend fun getAdditionGroupList(
        token: String,
        refreshing: Boolean = false
    ): List<AdditionGroup>

    suspend fun getAdditionGroup(additionUuid: String): AdditionGroup?

    /*UPDATE*/
    suspend fun updateAdditionGroup(
        updateAdditionGroup: UpdateAdditionGroup,
        token: String,
        additionGroupUuid: String
    )

    /*POST*/
    suspend fun postAdditionGroup(token: String, createAdditionGroup: CreateAdditionGroup): CreateAdditionGroup

    /*DELETE*/
    fun clearCache()
}
