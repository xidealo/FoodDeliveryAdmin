package com.bunbeauty.domain.repository.cafe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.CafeEntity
import com.bunbeauty.domain.repository.BaseDao

@Dao
interface CafeDao : BaseDao<CafeEntity> {

    @Query("SELECT * FROM CafeEntity")
    fun getCafeListLiveData(): LiveData<List<Cafe>>

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()
}