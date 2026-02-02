package com.bunbeauty.data.work

class IosNotificationTokenUpdater(
    private val interactor: UpdateNotificationTokenInteractor,
) {
    suspend fun update(notificationToken: String) {
        interactor.update(notificationToken)
    }
}
