package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.address.Address

interface AddressRepo {
    suspend fun insert(address: Address): Long
}