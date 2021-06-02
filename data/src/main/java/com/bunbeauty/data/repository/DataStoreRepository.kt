package com.bunbeauty.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
    private val Context.cafeUuidDataStore: DataStore<Preferences> by preferencesDataStore(name = CAFE_UUID_DATA_STORE)
    private val Context.deliveryDataStore: DataStore<Preferences> by preferencesDataStore(name = DELIVERY_DATA_STORE)

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

    override val cafeUuid: Flow<String> = context.cafeUuidDataStore.data.map {
        it[CAFE_UUID_KEY] ?: ""
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

    override suspend fun clearCache() {
        withContext(IO) {
            context.tokenDataStore.edit {
                it.clear()
            }
            context.cafeUuidDataStore.edit {
                it.clear()
            }
        }
    }

    companion object {

        // NAMES
        private const val TOKEN_DATA_STORE = "token dataStore"
        private const val CAFE_UUID_DATA_STORE = "cafe uuid dataStore"
        private const val DELIVERY_DATA_STORE = "delivery dataStore"
        private const val TOKEN = "token"
        private const val CAFE_UUID = "cafe uuid"
        private const val DELIVERY_COST = "delivery cost"
        private const val DELIVERY_FOR_FREE = "delivery for free"

        // KEYS
        private val TOKEN_KEY = stringPreferencesKey(TOKEN)
        private val CAFE_UUID_KEY = stringPreferencesKey(CAFE_UUID)
        private val DELIVERY_COST_KEY = intPreferencesKey(DELIVERY_COST)
        private val DELIVERY_FOR_FREE_KEY = intPreferencesKey(DELIVERY_FOR_FREE)
    }
}