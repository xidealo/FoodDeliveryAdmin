package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.CreateAddition
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.model.menuproduct.MenuProductPost

interface AdditionRepo {
    /* POST */
    suspend fun post(token: String, createAddition: CreateAddition)

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
    suspend fun clearCache()
}
