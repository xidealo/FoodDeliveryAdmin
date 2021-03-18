package com.bunbeauty.domain.repository.address

import com.bunbeauty.data.model.Address

interface AddressRepo {
    suspend fun insert(address: Address): Long
}