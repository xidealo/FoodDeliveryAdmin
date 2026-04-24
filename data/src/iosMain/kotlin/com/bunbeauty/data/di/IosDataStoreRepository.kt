package com.bunbeauty.data.di

import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

class IosDataStoreRepository : DataStoreRepo {
    private val defaults = NSUserDefaults.standardUserDefaults

    private val tokenState = MutableStateFlow<String?>(null)
    private val companyUuidState = MutableStateFlow("")
    private val usernameState = MutableStateFlow("")
    private val cafeUuidState = MutableStateFlow<String?>(null)
    private val previousCafeUuidState = MutableStateFlow<String?>(null)
    private val isUnlimitedNotificationState = MutableStateFlow(true)

    init {
        reloadFromDefaults()
    }

    override val token: Flow<String?> = tokenState.asStateFlow()

    override suspend fun getToken(): String? = defaults.stringForKey(KEY_TOKEN)

    override suspend fun saveToken(token: String) {
        defaults.setObject(token, forKey = KEY_TOKEN)
        tokenState.value = token
    }

    override val companyUuid: Flow<String> = companyUuidState.asStateFlow()

    override suspend fun saveCompanyUuid(companyUuid: String) {
        defaults.setObject(companyUuid, forKey = KEY_COMPANY_UUID)
        companyUuidState.value = companyUuid
    }

    override val username: Flow<String> = usernameState.asStateFlow()

    override suspend fun saveUsername(username: String) {
        defaults.setObject(username, forKey = KEY_USERNAME)
        usernameState.value = username
    }

    override val cafeUuid: Flow<String?> = cafeUuidState.asStateFlow()

    override suspend fun saveCafeUuid(cafeUuid: String) {
        defaults.setObject(cafeUuid, forKey = KEY_CAFE_UUID)
        cafeUuidState.value = cafeUuid
    }

    override val previousCafeUuid: Flow<String?> = previousCafeUuidState.asStateFlow()

    override suspend fun savePreviousCafeUuid(cafeUuid: String) {
        defaults.setObject(cafeUuid, forKey = KEY_PREVIOUS_CAFE_UUID)
        previousCafeUuidState.value = cafeUuid
    }

    override val isUnlimitedNotification: Flow<Boolean> = isUnlimitedNotificationState.asStateFlow()

    override suspend fun saveIsUnlimitedNotification(isUnlimitedNotification: Boolean) {
        defaults.setBool(isUnlimitedNotification, forKey = KEY_IS_UNLIMITED_NOTIFICATION)
        isUnlimitedNotificationState.value = isUnlimitedNotification
    }

    override suspend fun clearCache() {
        defaults.removeObjectForKey(KEY_TOKEN)
        defaults.removeObjectForKey(KEY_USERNAME)
        defaults.removeObjectForKey(KEY_COMPANY_UUID)
        defaults.removeObjectForKey(KEY_CAFE_UUID)
        defaults.removeObjectForKey(KEY_PREVIOUS_CAFE_UUID)
        tokenState.value = null
        companyUuidState.value = ""
        usernameState.value = ""
        cafeUuidState.value = null
        previousCafeUuidState.value = null
    }

    private fun reloadFromDefaults() {
        tokenState.value = defaults.stringForKey(KEY_TOKEN)
        companyUuidState.value = defaults.stringForKey(KEY_COMPANY_UUID).orEmpty()
        usernameState.value = defaults.stringForKey(KEY_USERNAME).orEmpty()
        cafeUuidState.value = defaults.stringForKey(KEY_CAFE_UUID)
        previousCafeUuidState.value = defaults.stringForKey(KEY_PREVIOUS_CAFE_UUID)
        isUnlimitedNotificationState.value = readIsUnlimitedNotificationFromDefaults()
    }

    private fun readIsUnlimitedNotificationFromDefaults(): Boolean {
        if (defaults.objectForKey(KEY_IS_UNLIMITED_NOTIFICATION) == null) {
            return true
        }
        return defaults.boolForKey(KEY_IS_UNLIMITED_NOTIFICATION)
    }

    private companion object {
        private const val USER_DATA_STORE = "user dataStore"
        private const val CAFE_UUID_DATA_STORE = "cafe uuid dataStore"
        private const val IS_UNLIMITED_NOTIFICATION_DATA_STORE =
            "is unlimited notification dataStore"

        private fun key(
            storeName: String,
            preferenceName: String,
        ): String = "$storeName::$preferenceName"

        private val KEY_TOKEN = key(USER_DATA_STORE, "token")
        private val KEY_USERNAME = key(USER_DATA_STORE, "username")
        private val KEY_COMPANY_UUID = key(USER_DATA_STORE, "company uuid")
        private val KEY_CAFE_UUID = key(CAFE_UUID_DATA_STORE, "cafe uuid")
        private val KEY_PREVIOUS_CAFE_UUID = key(CAFE_UUID_DATA_STORE, "previous cafe uuid")
        private val KEY_IS_UNLIMITED_NOTIFICATION =
            key(IS_UNLIMITED_NOTIFICATION_DATA_STORE, "company uuid")
    }
}
