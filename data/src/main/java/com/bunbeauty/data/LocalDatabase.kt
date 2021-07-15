package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.entity.CafeEntity

@Database(
    entities = [
        CafeEntity::class,
    ], version = 17
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
}