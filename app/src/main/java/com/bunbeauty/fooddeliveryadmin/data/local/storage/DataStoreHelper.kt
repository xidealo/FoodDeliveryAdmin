package com.bunbeauty.fooddeliveryadmin.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelper @Inject constructor(context: Context) : IDataStoreHelper {

    private val tokenDataStore = context.createDataStore(TOKEN_DATA_STORE)
    private val cafeIdDataStore = context.createDataStore(CAFE_ID_DATA_STORE)

    override val token: Flow<String> = tokenDataStore.data.map {
        it[TOKEN_KEY] ?: ""
    }

    override suspend fun saveToken(token: String) {
        tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override val cafeId: Flow<String> = cafeIdDataStore.data.map {
        it[CAFE_ID_KEY] ?: "0cafe"
    }

    override suspend fun saveCafeId(cafeId: String) {
        cafeIdDataStore.edit {
            it[CAFE_ID_KEY] = cafeId
        }
    }

    override suspend fun clearCache() {
        tokenDataStore.edit {
            it.clear()
        }
        cafeIdDataStore.edit {
            it.clear()
        }
    }

    companion object {

        // NAMES
        private const val TOKEN_DATA_STORE = "token dataStore"
        private const val CAFE_ID_DATA_STORE = "cafe id dataStore"
        private const val TOKEN = "token"
        private const val CAFE_ID = "cafe id"

        // KEYS
        private val TOKEN_KEY = stringPreferencesKey(TOKEN)
        private val CAFE_ID_KEY = stringPreferencesKey(CAFE_ID)
    }
}