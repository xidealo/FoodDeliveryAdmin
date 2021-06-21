package com.bunbeauty.data.dao

import androidx.room.Dao
import com.bunbeauty.domain.model.address.Address

@Dao
interface AddressDao: BaseDao<Address> {
}