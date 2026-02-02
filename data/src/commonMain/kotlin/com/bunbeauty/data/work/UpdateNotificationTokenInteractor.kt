package com.bunbeauty.data.work

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.DataStoreRepo

class UpdateNotificationTokenInteractor(
    private val dataStoreRepo: DataStoreRepo,
    private val foodDeliveryApi: FoodDeliveryApi,
) {
    suspend fun update(notificationToken: String) {
        val token =
            dataStoreRepo.getToken()
                ?: throw NoTokenException()

        foodDeliveryApi.putNotificationToken(
            updateNotificationTokenRequest =
                UpdateNotificationTokenRequest(
                    token = notificationToken,
                ),
            token = token,
        )
    }
}
