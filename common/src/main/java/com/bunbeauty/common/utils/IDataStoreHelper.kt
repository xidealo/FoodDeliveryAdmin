package com.bunbeauty.common.utils

import kotlinx.coroutines.flow.Flow

interface IDataStoreHelper {

    /**
     * It is auth token
     * Need to verify user
     * Lifetime 14 days
     */
    val token: Flow<String>
    suspend fun saveToken(token: String)

    val cafeId: Flow<String>
    suspend fun saveCafeId(cafeId: String)

    suspend fun clearCache()
}