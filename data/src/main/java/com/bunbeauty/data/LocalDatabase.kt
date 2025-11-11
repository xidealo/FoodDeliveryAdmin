package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CityDao
import com.bunbeauty.data.dao.NonWorkingDayDao
import com.bunbeauty.data.model.entity.CityEntity
import com.bunbeauty.data.model.entity.NonWorkingDayEntity

@Database(
    entities = [
        CityEntity::class,
        NonWorkingDayEntity::class,
    ],
    version = 29,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    abstract fun nonWorkingDayDao(): NonWorkingDayDao
}
