package com.bunbeauty.data

interface NotificationService {

    fun subscribeOnCafeNotification(cafeUuid: String)

    fun unsubscribeFromCafeNotification(cafeUuid: String)

}