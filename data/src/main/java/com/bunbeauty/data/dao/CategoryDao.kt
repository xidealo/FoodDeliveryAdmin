package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.entity.CategoryEntity
@Dao
interface CategoryDao : BaseDao<CategoryEntity>{
    @Transaction
    @Query("DELETE FROM CategoryEntity")
    suspend fun deleteAll()
}