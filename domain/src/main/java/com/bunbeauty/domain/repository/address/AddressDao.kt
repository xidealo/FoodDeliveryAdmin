package com.bunbeauty.domain.repository.address

import androidx.room.Dao
import com.bunbeauty.data.model.Address
import com.bunbeauty.domain.repository.BaseDao

@Dao
interface AddressDao: BaseDao<Address> {
}