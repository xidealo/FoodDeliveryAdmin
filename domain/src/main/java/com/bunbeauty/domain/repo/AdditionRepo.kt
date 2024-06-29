package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.UpdateAddition

interface AdditionRepo {
    /*GET*/
    suspend fun getAdditionCacheList(token: String): List<Addition>

    suspend fun getAdditionListFromRemote(
        token: String,
        takeRemote: Boolean = true
    ): List<Addition>

    suspend fun getAddition(additionUuid: String, token: String): Addition?

    /*UPDATE*/
    suspend fun updateAddition(
        updateAddition: UpdateAddition,
        token: String,
        additionUuid: String
    )

    /*DELETE*/
    suspend fun clearCache()
}
