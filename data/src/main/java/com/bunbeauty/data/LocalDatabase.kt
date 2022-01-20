package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.dao.CategoryDao
import com.bunbeauty.data.dao.MenuProductCategoryDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.entity.CategoryEntity
import com.bunbeauty.data.model.entity.menu_product.MenuProductCategoryEntity
import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity

@Database(
    entities = [
        CafeEntity::class,
        MenuProductEntity::class,
        CategoryEntity::class,
        MenuProductCategoryEntity::class
    ], version = 22
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
    abstract fun menuProductDao(): MenuProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun menuProductCategoryDao(): MenuProductCategoryDao
}