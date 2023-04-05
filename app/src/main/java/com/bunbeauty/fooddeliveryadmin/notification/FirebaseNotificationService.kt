package com.bunbeauty.fooddeliveryadmin.notification

import android.util.Log
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.data.NotificationService
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import javax.inject.Inject

class FirebaseNotificationService @Inject constructor(): NotificationService {

    override fun subscribeOnCafeNotification(cafeUuid: String) {
        Firebase.messaging.subscribeToTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Subscribed to $cafeUuid successfully")
                } else {
                    Log.e(NOTIFICATION_TAG, "Failed to subscribe to $cafeUuid")
                }
            }
    }

    override fun unsubscribeFromCafeNotification(cafeUuid: String) {
        Firebase.messaging.unsubscribeFromTopic(cafeUuid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Unsubscribed from $cafeUuid successfully")
                } else {
                    Log.e(NOTIFICATION_TAG, "Failed to unsubscribe from $cafeUuid")
                }
            }
    }

}