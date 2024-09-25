package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.entity.menuproduct.MenuProductCategoryEntity

@Dao
interface MenuProductCategoryDao : BaseDao<MenuProductCategoryEntity> {
    @Transaction
    @Query("DELETE FROM MenuProductCategoryEntity")
    suspend fun deleteAll()
}
