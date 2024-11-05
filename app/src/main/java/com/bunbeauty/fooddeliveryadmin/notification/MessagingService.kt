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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.Constants.CHANNEL_ID
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class MessagingService : FirebaseMessagingService(), LifecycleOwner {

    private val serviceDispatcher = ServiceLifecycleDispatcher(this)

    override val lifecycle: Lifecycle
        get() = serviceDispatcher.lifecycle

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    override fun onCreate() {
        serviceDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(NOTIFICATION_TAG, "onMessageReceived")

        val isNotificationPermissionGranted =
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (isNotificationPermissionGranted) {
            val notification = remoteMessage.notification ?: return
            lifecycleScope.launch {
                showNotification(
                    remoteNotification = notification,
                    isUnlimited = dataStoreRepo.getIsUnlimitedNotification()
                )
            }
        }
    }

    override fun onDestroy() {
        serviceDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun showNotification(
        remoteNotification: RemoteMessage.Notification,
        isUnlimited: Boolean
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_new_order)
            .setContentTitle(remoteNotification.title)
            .setContentText(remoteNotification.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setColor(ContextCompat.getColor(this, R.color.lightIconColor))
        val notification = builder.build().apply {
            if (isUnlimited) {
                flags = flags or Notification.FLAG_INSISTENT
            }
        }
        val id = remoteNotification.title.hashCode()
        notificationManagerCompat.notify(id, notification)
    }
}
