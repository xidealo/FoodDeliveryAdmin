package com.bunbeauty.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val context: Context,
) : DataStoreRepo {
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATA_STORE)
    private val Context.cafeUuidDataStore: DataStore<Preferences> by preferencesDataStore(name = CAFE_UUID_DATA_STORE)
    private val Context.isUnlimitedNotification: DataStore<Preferences> by preferencesDataStore(name = IS_UNLIMITED_NOTIFICATION_DATA_STORE)

    override val token =
        context.userDataStore.data.map {
            it[TOKEN_KEY]
        }

    override suspend fun getToken(): String? = token.firstOrNull()

    override suspend fun saveToken(token: String) {
        context.userDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    override val companyUuid: Flow<String> =
        context.userDataStore.data.map {
            it[COMPANY_UUID_KEY] ?: ""
        }

    override suspend fun saveCompanyUuid(companyUuid: String) {
        context.userDataStore.edit {
            it[COMPANY_UUID_KEY] = companyUuid
        }
    }

    override val username =
        context.userDataStore.data.map {
            it[USERNAME_KEY] ?: ""
        }

    override suspend fun saveUsername(username: String) {
        context.userDataStore.edit {
            it[USERNAME_KEY] = username
        }
    }

    override val cafeUuid: Flow<String?> =
        context.cafeUuidDataStore.data.map {
            it[CAFE_UUID_KEY]
        }

    override suspend fun saveCafeUuid(cafeUuid: String) {
        context.cafeUuidDataStore.edit {
            it[CAFE_UUID_KEY] = cafeUuid
        }
    }

    override val previousCafeUuid: Flow<String?> =
        context.cafeUuidDataStore.data.map {
            it[PREVIOUS_CAFE_UUID_KEY]
        }

    override suspend fun savePreviousCafeUuid(cafeUuid: String) {
        context.cafeUuidDataStore.edit {
            it[PREVIOUS_CAFE_UUID_KEY] = cafeUuid
        }
    }

    override val isUnlimitedNotification: Flow<Boolean> =
        context.isUnlimitedNotification.data.map {
            it[IS_UNLIMITED_NOTIFICATION_KEY] ?: true
        }

    override suspend fun saveIsUnlimitedNotification(isUnlimitedNotification: Boolean) {
        context.isUnlimitedNotification.edit {
            it[IS_UNLIMITED_NOTIFICATION_KEY] = isUnlimitedNotification
        }
    }

    override suspend fun clearCache() {
        context.userDataStore.edit {
            it.clear()
        }
        context.cafeUuidDataStore.edit {
            it.clear()
        }
    }

    companion object {
        // NAMES
        private const val USER_DATA_STORE = "user dataStore"
        private const val CAFE_UUID_DATA_STORE = "cafe uuid dataStore"
        private const val IS_UNLIMITED_NOTIFICATION_DATA_STORE =
            "is unlimited notification dataStore"

        private const val TOKEN = "token"
        private const val USERNAME = "username"
        private const val COMPANY_UUID = "company uuid"
        private const val CAFE_UUID = "cafe uuid"
        private const val PREVIOUS_CAFE_UUID = "previous cafe uuid"
        private const val IS_UNLIMITED_NOTIFICATION = "company uuid"

        // KEYS
        private val TOKEN_KEY = stringPreferencesKey(TOKEN)
        private val USERNAME_KEY = stringPreferencesKey(USERNAME)
        private val COMPANY_UUID_KEY = stringPreferencesKey(COMPANY_UUID)
        private val CAFE_UUID_KEY = stringPreferencesKey(CAFE_UUID)
        private val PREVIOUS_CAFE_UUID_KEY = stringPreferencesKey(PREVIOUS_CAFE_UUID)
        private val IS_UNLIMITED_NOTIFICATION_KEY = booleanPreferencesKey(IS_UNLIMITED_NOTIFICATION)
    }
}
