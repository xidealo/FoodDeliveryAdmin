package com.bunbeauty.fooddeliveryadmin.data.local.db.address

import com.bunbeauty.data.model.Address

interface AddressRepo {

    suspend fun insert(address: Address): Long
}