package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.UpdateAddition

interface AdditionRepo {
    /*GET*/
    suspend fun getAdditionList(
        token: String,
        takeRemote: Boolean = true
    ): List<Addition>

    suspend fun getAddition(additionUuid: String): Addition?

    /*UPDATE*/
    suspend fun updateAddition(
        updateAddition: UpdateAddition,
        token: String,
        additionUuid: String
    )

    /*DELETE*/
    suspend fun clearCache()
}
