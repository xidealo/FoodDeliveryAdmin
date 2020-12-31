package com.bunbeauty.fooddeliveryadmin.data.local.storage

import kotlinx.coroutines.flow.Flow

interface IDataStoreHelper {

    /**
     * It is auth token
     * Need to verify user
     * Lifetime 14 days
     */
    val token: Flow<String>
    suspend fun saveToken(token: String)
}