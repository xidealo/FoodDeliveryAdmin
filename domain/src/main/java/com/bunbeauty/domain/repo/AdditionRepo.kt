package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.model.addition.UpdateAddition

interface AdditionRepo {
    /* POST */
    suspend fun post(token: String, createAddition: CreateAddition): Addition?

    /*GET*/
    suspend fun getAdditionList(
        token: String,
        refreshing: Boolean = false
    ): List<Addition>

    suspend fun getAddition(additionUuid: String, token: String): Addition?

    /*UPDATE*/
    suspend fun updateAddition(
        updateAddition: UpdateAddition,
        token: String,
        additionUuid: String
    )

    /*DELETE*/
    fun clearCache()
}
