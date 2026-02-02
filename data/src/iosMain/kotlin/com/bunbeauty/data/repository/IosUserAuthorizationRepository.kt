package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.domain.repo.DataStoreRepo

class IosUserAuthorizationRepository(
    networkConnector: FoodDeliveryApi,
    dataStoreRepo: DataStoreRepo,
) : BaseUserAuthorizationRepository(
        networkConnector = networkConnector,
        dataStoreRepo = dataStoreRepo,
    ) {
    override fun updateNotificationToken() {
    }

    override fun updateNotificationToken(notificationToken: String) {
    }
}
