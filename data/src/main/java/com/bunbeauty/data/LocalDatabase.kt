package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.dao.CityDao
import com.bunbeauty.data.dao.NonWorkingDayDao
import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.entity.CityEntity
import com.bunbeauty.data.model.entity.NonWorkingDayEntity

@Database(
    entities = [
        CafeEntity::class,
        CityEntity::class,
        NonWorkingDayEntity::class
    ],
    version = 28
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
    abstract fun cityDao(): CityDao
    abstract fun nonWorkingDayDao(): NonWorkingDayDao
}
