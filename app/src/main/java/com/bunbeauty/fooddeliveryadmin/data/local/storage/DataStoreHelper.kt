package com.bunbeauty.fooddeliveryadmin.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreHelper @Inject constructor(context: Context) : IDataStoreHelper {

    private val tokenDataStore = context.createDataStore(TOKEN_DATA_STORE)

    override val token: Flow<String> = tokenDataStore.data.map {
        it[TOKEN_KEY] ?: ""
    }

    override suspend fun saveToken(token: String) {
        tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override suspend fun clearCache() {
        tokenDataStore.edit {
            it.clear()
        }
    }

    companion object {

        // NAMES
        private const val TOKEN_DATA_STORE = "Token data store"
        private const val TOKEN = "token"

        // KEYS
        private val TOKEN_KEY = preferencesKey<String>(TOKEN)
    }
}