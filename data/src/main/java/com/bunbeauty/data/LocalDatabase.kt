package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.dao.CategoryDao
import com.bunbeauty.data.dao.CityDao
import com.bunbeauty.data.dao.MenuProductCategoryDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.dao.NonWorkingDayDao
import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.entity.CategoryEntity
import com.bunbeauty.data.model.entity.CityEntity
import com.bunbeauty.data.model.entity.NonWorkingDayEntity
import com.bunbeauty.data.model.entity.menuproduct.MenuProductCategoryEntity
import com.bunbeauty.data.model.entity.menuproduct.MenuProductEntity

@Database(
    entities = [
        CafeEntity::class,
        MenuProductEntity::class,
        CategoryEntity::class,
        MenuProductCategoryEntity::class,
        CityEntity::class,
        NonWorkingDayEntity::class
    ],
    version = 26
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
    abstract fun menuProductDao(): MenuProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuProductCategoryDao(): MenuProductCategoryDao
    abstract fun cityDao(): CityDao
    abstract fun nonWorkingDayDao(): NonWorkingDayDao
}
