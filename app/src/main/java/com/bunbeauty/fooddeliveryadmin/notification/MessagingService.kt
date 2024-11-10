package com.bunbeauty.fooddeliveryadmin.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bunbeauty.common.Constants.CHANNEL_ID
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ORDER_CODE_KEY = "orderCode"

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    @Inject
    lateinit var userAuthorizationRepo: UserAuthorizationRepo

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    override fun onNewToken(token: String) {
        Log.d(NOTIFICATION_TAG, "onNewToken $token")
        userAuthorizationRepo.updateNotificationToken(notificationToken = token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(NOTIFICATION_TAG, "onMessageReceived")

        val isNotificationPermissionGranted =
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        Log.d(NOTIFICATION_TAG, "isNotificationPermissionGranted $isNotificationPermissionGranted")
        if (isNotificationPermissionGranted) {
            val orderCode = remoteMessage.data[ORDER_CODE_KEY] ?: run {
                Log.d(NOTIFICATION_TAG, "No $ORDER_CODE_KEY")
                return
            }
            showNotification(
                orderCode = orderCode,
                isUnlimited = true
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        orderCode: String,
        isUnlimited: Boolean
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_new_order)
            .setContentTitle(orderCode)
            .setContentText(resources.getString(R.string.msg_messaging_new_order))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setColor(ContextCompat.getColor(this, R.color.lightIconColor))
        val notification = builder.build().apply {
            if (isUnlimited) {
                flags = flags or Notification.FLAG_INSISTENT
            }
        }
        val id = orderCode.hashCode()
        notificationManagerCompat.notify(id, notification)
    }
}
