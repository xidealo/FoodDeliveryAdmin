package com.bunbeauty.data.di

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class IosStubDataStoreRepository : DataStoreRepo {
    private val tokenState = MutableStateFlow<String?>(null)
    private val companyUuidState = MutableStateFlow("")
    private val usernameState = MutableStateFlow("")
    private val cafeUuidState = MutableStateFlow<String?>(null)
    private val previousCafeUuidState = MutableStateFlow<String?>(null)
    private val isUnlimitedNotificationState = MutableStateFlow(false)

    override val token: Flow<String?> = tokenState

    override suspend fun getToken(): String? = tokenState.value

    override suspend fun saveToken(token: String) {
        tokenState.value = token
    }

    override val companyUuid: Flow<String> = companyUuidState

    override suspend fun saveCompanyUuid(companyUuid: String) {
        companyUuidState.value = companyUuid
    }

    override val username: Flow<String> = usernameState

    override suspend fun saveUsername(username: String) {
        usernameState.value = username
    }

    override val cafeUuid: Flow<String?> = cafeUuidState

    override suspend fun saveCafeUuid(cafeUuid: String) {
        cafeUuidState.value = cafeUuid
    }

    override val previousCafeUuid: Flow<String?> = previousCafeUuidState

    override suspend fun savePreviousCafeUuid(cafeUuid: String) {
        previousCafeUuidState.value = cafeUuid
    }

    override val isUnlimitedNotification: Flow<Boolean> = isUnlimitedNotificationState

    override suspend fun saveIsUnlimitedNotification(isUnlimitedNotification: Boolean) {
        isUnlimitedNotificationState.value = isUnlimitedNotification
    }

    override suspend fun clearCache() {
        tokenState.value = null
        companyUuidState.value = ""
        usernameState.value = ""
        cafeUuidState.value = null
        previousCafeUuidState.value = null
        isUnlimitedNotificationState.value = false
    }
}

class IosStubUserAuthorizationRepository : UserAuthorizationRepo {
    override suspend fun login(
        username: String,
        password: String,
    ): LoginUser? = null

    override fun updateNotificationToken() = Unit

    override fun updateNotificationToken(notificationToken: String) = Unit

    override suspend fun clearNotificationToken() = Unit
}

class IosStubPhotoRepository : PhotoRepo {
    override suspend fun getPhotoList(username: String): List<Photo> = emptyList()

    override suspend fun fetchPhotoList(username: String): List<Photo> = emptyList()

    override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? = null

    override suspend fun deletePhoto(photoLink: String) = Unit

    override fun clearCache() = Unit
}
