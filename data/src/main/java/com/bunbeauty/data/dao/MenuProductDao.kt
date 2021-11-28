package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.entity.MenuProductEntity
import com.bunbeauty.domain.model.menu_product.MenuProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuProductDao : BaseDao<MenuProductEntity> {

    @Query("SELECT * FROM MenuProductEntity")
    fun getListFlow(): Flow<List<MenuProductEntity>>

    @Query("SELECT * FROM MenuProductEntity")
    fun getList(): List<MenuProductEntity>

    @Query("SELECT * FROM MenuProductEntity WHERE uuid = :uuid")
    fun getByUuid(uuid: String): Flow<MenuProductEntity?>

    @Transaction
    @Query("DELETE FROM MenuProductEntity")
    suspend fun deleteAll()
}