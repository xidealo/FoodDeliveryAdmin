package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity
import com.bunbeauty.data.model.entity.menu_product.MenuProductWithCategoriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuProductDao : BaseDao<MenuProductEntity> {

    @Query("SELECT * FROM MenuProductEntity")
    fun getListFlow(): Flow<List<MenuProductWithCategoriesEntity>>

    @Query("SELECT * FROM MenuProductEntity")
    fun getList(): List<MenuProductEntity>

    @Query("SELECT * FROM MenuProductEntity WHERE uuid = :uuid")
    fun getByUuid(uuid: String): Flow<MenuProductEntity?>

    @Transaction
    @Query("DELETE FROM MenuProductEntity")
    suspend fun deleteAll()
}