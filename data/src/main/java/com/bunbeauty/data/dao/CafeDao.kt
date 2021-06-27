package com.bunbeauty.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CafeDao : BaseDao<CafeEntity> {

    @Query("SELECT * FROM CafeEntity")
    fun getCafeList(): Flow<List<CafeEntity>>

    @Query("SELECT * FROM CafeEntity WHERE uuid = :uuid")
    fun getCafeByUuid(uuid: String): Flow<CafeEntity?>

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()
}