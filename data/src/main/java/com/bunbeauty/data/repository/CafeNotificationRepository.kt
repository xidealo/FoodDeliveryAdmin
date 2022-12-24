package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import javax.inject.Inject

class CafeNotificationRepository @Inject constructor() {

    fun subscribeOnCafeNotification(cafeUuid: String) {
        Firebase.messaging.subscribeToTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Constants.NOTIFICATION_TAG, "Subscription to $cafeUuid is successful")
                } else {
                    Log.d(Constants.NOTIFICATION_TAG, "Subscription to $cafeUuid failed")
                }
            }
    }

    fun unsubscribeFromCafeNotification(cafeUuid: String) {
        Firebase.messaging.unsubscribeFromTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Constants.NOTIFICATION_TAG, "Unsubscription from $cafeUuid is successful")
                } else {
                    Log.d(Constants.NOTIFICATION_TAG, "Unsubscription from $cafeUuid failed")
                }
            }
    }
}