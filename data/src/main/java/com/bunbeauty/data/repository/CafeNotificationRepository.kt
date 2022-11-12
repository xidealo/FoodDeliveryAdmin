package com.bunbeauty.data.repository

import com.bunbeauty.data.NetworkConnector
import javax.inject.Inject

class CafeNotificationRepository @Inject constructor(
    private val networkConnector: NetworkConnector
) {

    suspend fun subscribeOnCafeNotification(cafeUuid: String) {
        networkConnector.subscribeOnCafeTopic(cafeUuid)
    }

    suspend fun unsubscribeFromCafeNotification(cafeUuid: String) {
        networkConnector.unsubscribeFromCafeTopic(cafeUuid)
    }
}