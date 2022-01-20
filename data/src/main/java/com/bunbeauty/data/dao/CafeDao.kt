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

    @Query("SELECT * FROM CafeEntity")
    fun getCafeList(): List<CafeEntity>

    @Query("SELECT * FROM CafeEntity WHERE cityUuid =:cityUuid")
    fun getCafeList(cityUuid:String): List<CafeEntity>

    @Query("SELECT * FROM CafeEntity WHERE uuid = :uuid")
    fun getCafeByUuid(uuid: String): Flow<CafeEntity?>

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()
}