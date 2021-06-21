package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CafeDao : BaseDao<CafeEntity> {

    @Query("SELECT * FROM CafeEntity")
    fun getCafeList(): Flow<List<Cafe>>

    @Query("SELECT * FROM CafeEntity WHERE id = :cafeId")
    fun getCafeById(cafeId: String): Cafe?

    @Query("SELECT * FROM CafeEntity WHERE id = :cafeId")
    fun getCafeByIdFlow(cafeId: String): Flow<Cafe?>

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()
}