package com.bunbeauty.fooddeliveryadmin.notification

import android.util.Log
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.domain.NotificationService
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import javax.inject.Inject

class FirebaseNotificationService @Inject constructor(): NotificationService {

    override fun subscribeOnNotifications(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Subscribed to $topic successfully")
                } else {
                    Log.e(NOTIFICATION_TAG, "Failed to subscribe to $topic")
                }
            }
    }

    override fun unsubscribeFromNotifications(topic: String) {
        Firebase.messaging.unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(NOTIFICATION_TAG, "Unsubscribed from $topic successfully")
                } else {
                    Log.e(NOTIFICATION_TAG, "Failed to unsubscribe from $topic")
                }
            }
    }

}