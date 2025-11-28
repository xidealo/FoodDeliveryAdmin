package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.bunbeauty.data.model.entity.NonWorkingDayEntity

@Dao
interface NonWorkingDayDao : BaseDao<NonWorkingDayEntity> {

    @Query("SELECT * FROM NonWorkingDayEntity WHERE cafeUuid =:cafeUuid")
    suspend fun getNonWorkingDayListByCafeUuid(cafeUuid: String): List<NonWorkingDayEntity>
}
