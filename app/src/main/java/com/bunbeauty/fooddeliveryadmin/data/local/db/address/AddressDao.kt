package com.bunbeauty.fooddeliveryadmin.data.local.db.address

import androidx.room.Dao
import com.bunbeauty.fooddeliveryadmin.data.local.db.BaseDao
import com.bunbeauty.data.model.Address

@Dao
interface AddressDao: BaseDao<Address> {
}