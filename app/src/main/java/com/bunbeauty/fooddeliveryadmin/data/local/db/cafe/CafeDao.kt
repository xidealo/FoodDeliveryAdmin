package com.bunbeauty.fooddeliveryadmin.data.local.db.cafe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bunbeauty.fooddeliveryadmin.data.local.db.BaseDao
import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.CafeEntity

@Dao
interface CafeDao : BaseDao<CafeEntity> {

    @Query("SELECT * FROM CafeEntity")
    fun getCafeListLiveData(): LiveData<List<Cafe>>

    @Transaction
    @Query("DELETE FROM CafeEntity")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(address: Address)

    @Transaction
    suspend fun insertCafe(cafe: Cafe) {
        insert(cafe.cafeEntity)

        cafe.address.cafeId = cafe.cafeEntity.id
        insert(cafe.address)
    }
}