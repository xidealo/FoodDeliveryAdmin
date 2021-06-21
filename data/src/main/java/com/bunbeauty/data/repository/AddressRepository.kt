package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.address.Address
import com.bunbeauty.domain.repo.AddressRepo
import com.bunbeauty.data.dao.AddressDao
import javax.inject.Inject

class AddressRepository @Inject constructor(private val addressDao: AddressDao):
    AddressRepo {

    override suspend fun insert(address: Address): Long {
        return addressDao.insert(address)
    }
}