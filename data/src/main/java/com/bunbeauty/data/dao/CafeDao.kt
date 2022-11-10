package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.entity.CafeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CafeDao : BaseDao<CafeEntity> {

    @Query("SELECT * FROM CafeEntity")
    fun getCafeListFlow(): Flow<List<CafeEntity>>

    @Query("SELECT * FROM CafeEntity WHERE cityUuid =:cityUuid")
    fun getCafeListByCityUuid(cityUuid:String): List<CafeEntity>

    @Query("SELECT * FROM CafeEntity WHERE uuid = :uuid")
    fun observeCafeByUuid(uuid: String): Flow<CafeEntity?>

    @Query("SELECT * FROM CafeEntity WHERE uuid = :uuid")
    suspend fun getCafeByUuid(uuid: String): CafeEntity?

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()
}