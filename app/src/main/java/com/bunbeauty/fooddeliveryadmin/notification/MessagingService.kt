package com.bunbeauty.fooddeliveryadmin.notification

import android.Manifest
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
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val LAST_ORDER_NOTIFICATION_ID = 1
private const val ORDER_CODE_KEY = "orderCode"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class MessagingService : FirebaseMessagingService(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        Log.d("MessagingService", throwable.message.toString())
    }

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(NOTIFICATION_TAG, "onMessageReceived")

        val isNotificationPermissionGranted =
            (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                    (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED)
        if (isNotificationPermissionGranted) {
            val code = remoteMessage.data[ORDER_CODE_KEY] ?: ""
            showNotification(code)

            launch(coroutineExceptionHandler) {
                dataStoreRepo.saveLastOrderCode(code)
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun showNotification(code: String) {
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
            .setContentTitle("${resources.getString(R.string.title_messaging_new_order)} $code")
            .setContentText(resources.getString(R.string.msg_messaging_new_order))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setColor(ContextCompat.getColor(this, R.color.lightIconColor))


        NotificationManagerCompat.from(this)
            .notify(LAST_ORDER_NOTIFICATION_ID, builder.build()
                .apply {
                    flags = flags or Notification.FLAG_INSISTENT
                }
            )
    }
}
