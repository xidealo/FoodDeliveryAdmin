package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.bunbeauty.data.model.entity.city.CityEntity

@Dao
interface CityDao : BaseDao<CityEntity> {

    @Query("SELECT * FROM CityEntity")
    suspend fun getCityList(): List<CityEntity>

}