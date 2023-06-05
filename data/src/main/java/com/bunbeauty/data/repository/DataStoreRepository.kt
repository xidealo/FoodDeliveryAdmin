package com.bunbeauty.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.repo.DataStoreRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) :
    DataStoreRepo {

    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_DATA_STORE)
    private val Context.managerCityDataStore: DataStore<Preferences> by preferencesDataStore(name = MANAGER_CITY_UUID_DATA_STORE)
    private val Context.cafeUuidDataStore: DataStore<Preferences> by preferencesDataStore(name = CAFE_UUID_DATA_STORE)
    private val Context.deliveryDataStore: DataStore<Preferences> by preferencesDataStore(name = DELIVERY_DATA_STORE)
    private val Context.companyUuidDataStore: DataStore<Preferences> by preferencesDataStore(name = COMPANY_UUID_DATA_STORE)
    private val Context.lastOrderCodeDataStore: DataStore<Preferences> by preferencesDataStore(name = LAST_ORDER_CODE_STORE)
    private val Context.isUnlimitedNotification: DataStore<Preferences> by preferencesDataStore(name = IS_UNLIMITED_NOTIFICATION_DATA_STORE)

    override val token: Flow<String> = context.tokenDataStore.data.map {
        it[TOKEN_KEY] ?: ""
    }

    override suspend fun saveToken(token: String) {
        withContext(IO) {
            context.tokenDataStore.edit {
                it[TOKEN_KEY] = token
            }
        }
    }

    override val managerCity: Flow<String> = context.managerCityDataStore.data.map {
        it[MANAGER_CITY_UUID_KEY] ?: ""
    }

    override suspend fun saveManagerCity(managerCity: String) {
        withContext(IO) {
            context.managerCityDataStore.edit {
                it[MANAGER_CITY_UUID_KEY] = managerCity
            }
        }
    }

    override val cafeUuid: Flow<String?> = context.cafeUuidDataStore.data.map {
        it[CAFE_UUID_KEY]
    }

    override suspend fun saveCafeUuid(cafeUuid: String) {
        withContext(IO) {
            context.cafeUuidDataStore.edit {
                it[CAFE_UUID_KEY] = cafeUuid
            }
        }
    }

    override val delivery: Flow<Delivery> = context.deliveryDataStore.data.map {
        Delivery(
            it[DELIVERY_COST_KEY] ?: 0,
            it[DELIVERY_FOR_FREE_KEY] ?: 0
        )
    }

    override suspend fun saveDelivery(delivery: Delivery) {
        withContext(IO) {
            context.deliveryDataStore.edit {
                it[DELIVERY_COST_KEY] = delivery.cost
                it[DELIVERY_FOR_FREE_KEY] = delivery.forFree
            }
        }
    }

    override val companyUuid: Flow<String> = context.companyUuidDataStore.data.map {
        it[COMPANY_UUID_KEY] ?: ""
    }

    override suspend fun saveCompanyUuid(companyUuid: String) {
        withContext(IO) {
            context.companyUuidDataStore.edit {
                it[COMPANY_UUID_KEY] = companyUuid
            }
        }
    }

    override val lastOrderCode: Flow<String> = context.lastOrderCodeDataStore.data.map {
        it[LAST_ORDER_CODE_KEY] ?: ""
    }

    override suspend fun saveLastOrderCode(orderCode: String) {
        withContext(IO) {
            context.lastOrderCodeDataStore.edit {
                it[LAST_ORDER_CODE_KEY] = orderCode
            }
        }
    }

    override val isUnlimitedNotification: Flow<Boolean> = context.isUnlimitedNotification.data.map {
        it[IS_UNLIMITED_NOTIFICATION_KEY] ?: false
    }

    override suspend fun saveIsUnlimitedNotification(isUnlimitedNotification: Boolean) {
        withContext(IO) {
            context.isUnlimitedNotification.edit {
                it[IS_UNLIMITED_NOTIFICATION_KEY] = isUnlimitedNotification
            }
        }
    }

    override suspend fun clearCache() {
        withContext(IO) {
            context.tokenDataStore.edit {
                it.clear()
            }
            context.cafeUuidDataStore.edit {
                it.clear()
            }
            context.managerCityDataStore.edit {
                it.clear()
            }
        }
    }

    companion object {

        // NAMES
        private const val TOKEN_DATA_STORE = "token dataStore"
        private const val MANAGER_CITY_UUID_DATA_STORE = "manager city uuid dataStore"
        private const val CAFE_UUID_DATA_STORE = "cafe uuid dataStore"
        private const val DELIVERY_DATA_STORE = "delivery dataStore"
        private const val COMPANY_UUID_DATA_STORE = "company uuid dataStore"
        private const val LAST_ORDER_CODE_STORE = "last order code dataStore"
        private const val IS_UNLIMITED_NOTIFICATION_DATA_STORE =
            "is unlimited notification dataStore"
        private const val TOKEN = "token"
        private const val MANAGER_CITY_UUID = "manager city uuid"
        private const val CAFE_UUID = "cafe uuid"
        private const val DELIVERY_COST = "delivery cost"
        private const val DELIVERY_FOR_FREE = "delivery for free"
        private const val COMPANY_UUID = "company uuid"
        private const val LAST_ORDER_CODE = "company uuid"
        private const val IS_UNLIMITED_NOTIFICATION = "company uuid"

        // KEYS
        private val TOKEN_KEY = stringPreferencesKey(TOKEN)
        private val MANAGER_CITY_UUID_KEY = stringPreferencesKey(MANAGER_CITY_UUID)
        private val COMPANY_UUID_KEY = stringPreferencesKey(COMPANY_UUID)
        private val CAFE_UUID_KEY = stringPreferencesKey(CAFE_UUID)
        private val DELIVERY_COST_KEY = intPreferencesKey(DELIVERY_COST)
        private val DELIVERY_FOR_FREE_KEY = intPreferencesKey(DELIVERY_FOR_FREE)
        private val LAST_ORDER_CODE_KEY = stringPreferencesKey(LAST_ORDER_CODE)
        private val IS_UNLIMITED_NOTIFICATION_KEY = booleanPreferencesKey(IS_UNLIMITED_NOTIFICATION)
    }
}