package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.model.nonworkingday.NonWorkingDay

interface NonWorkingDayRepo {

    suspend fun getNonWorkingDayListByCafeUuid(cafeUuid: String): List<NonWorkingDay>
    suspend fun saveNonWorkingDay(token: String, newNonWorkingDay: NewNonWorkingDay): NonWorkingDay?
    suspend fun updateNonWorkingDay(token: String, uuid: String, isVisible: Boolean): NonWorkingDay?
    suspend fun clearCache()
}