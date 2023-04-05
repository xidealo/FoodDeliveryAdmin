package com.bunbeauty.domain

interface NotificationService {

    fun subscribeOnNotifications(topic: String)

    fun unsubscribeFromNotifications(topic: String)

}