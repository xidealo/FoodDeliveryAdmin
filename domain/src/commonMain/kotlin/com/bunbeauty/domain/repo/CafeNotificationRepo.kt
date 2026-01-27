package com.bunbeauty.domain.repo

interface CafeNotificationRepo {
    fun subscribeOnCafeNotification(cafeUuid: String)

    fun unsubscribeFromCafeNotification(cafeUuid: String)
}
