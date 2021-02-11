package com.bunbeauty.fooddeliveryadmin.data.local.db.address

import com.bunbeauty.fooddeliveryadmin.data.model.Address
import javax.inject.Inject

class AddressRepository @Inject constructor(private val addressDao: AddressDao): AddressRepo {

    override suspend fun insert(address: Address): Long {
        return addressDao.insert(address)
    }
}