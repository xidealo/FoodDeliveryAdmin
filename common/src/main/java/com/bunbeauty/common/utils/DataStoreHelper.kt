package com.bunbeauty.common.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelper @Inject constructor(private val context: Context) : IDataStoreHelper {

    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_DATA_STORE)
    private val Context.cafeIdDataStore: DataStore<Preferences> by preferencesDataStore(name = CAFE_ID_DATA_STORE)

    override val token: Flow<String> = context.tokenDataStore.data.map {
        it[TOKEN_KEY] ?: ""
    }

    override suspend fun saveToken(token: String) {
        Log.d("test", "saveToken $token")
        context.tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override val cafeId: Flow<String> = context.cafeIdDataStore.data.map {
        it[CAFE_ID_KEY] ?: ""
    }

    override suspend fun saveCafeId(cafeId: String) {
       context. cafeIdDataStore.edit {
            it[CAFE_ID_KEY] = cafeId
        }
    }

    override suspend fun clearCache() {
        context.tokenDataStore.edit {
            it.clear()
        }
        context.cafeIdDataStore.edit {
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