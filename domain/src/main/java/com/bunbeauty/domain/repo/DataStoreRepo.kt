package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    val token: Flow<String>
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)

    val managerCity: Flow<String>
    suspend fun saveManagerCity(managerCity: String)

    val cafeUuid: Flow<String?>
    suspend fun saveCafeUuid(cafeUuid: String)

    val delivery: Flow<Delivery>
    suspend fun saveDelivery(delivery: Delivery)

    val companyUuid: Flow<String>
    suspend fun saveCompanyUuid(companyUuid: String)

    suspend fun clearCache()
}