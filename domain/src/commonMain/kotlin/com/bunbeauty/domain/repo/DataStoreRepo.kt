package com.bunbeauty.domain.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    val token: Flow<String?>

    suspend fun getToken(): String?

    suspend fun saveToken(token: String)

    val companyUuid: Flow<String>

    suspend fun saveCompanyUuid(companyUuid: String)

    val username: Flow<String>

    suspend fun saveUsername(username: String)

    val cafeUuid: Flow<String?>

    suspend fun saveCafeUuid(cafeUuid: String)

    val previousCafeUuid: Flow<String?>

    suspend fun savePreviousCafeUuid(cafeUuid: String)

    val isUnlimitedNotification: Flow<Boolean>

    suspend fun saveIsUnlimitedNotification(isUnlimitedNotification: Boolean)

    suspend fun clearCache()
}
