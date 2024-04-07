package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct

interface AdditionRepo {
    /*GET*/
    suspend fun getAdditionList(
        token: String,
        takeRemote: Boolean = true
    ): List<Addition>

    suspend fun getAddition(additionUuid: String): Addition?

    /*UPDATE*/
    suspend fun updateAddition(
        token: String,
        addition: Addition,
    )

    /*DELETE*/
    suspend fun clearCache()
}
