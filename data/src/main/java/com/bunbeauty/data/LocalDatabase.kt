package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.entity.MenuProductEntity

@Database(
    entities = [
        CafeEntity::class,
        MenuProductEntity::class
    ], version = 19
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
    abstract fun menuProductDao(): MenuProductDao
}