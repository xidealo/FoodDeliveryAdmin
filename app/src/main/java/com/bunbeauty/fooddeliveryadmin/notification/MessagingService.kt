package com.bunbeauty.fooddeliveryadmin.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.BuildConfig
import com.bunbeauty.fooddeliveryadmin.Constants.CHANNEL_ID
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService(), CoroutineScope {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    override val coroutineContext = Job()

    override fun onCreate() {
        super.onCreate()

        (application as FoodDeliveryAdminApplication).appComponent.inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NotificationTag", "onMessageReceived")

        launch(IO) {
            val cafeId = dataStoreRepo.cafeId.first()
            if (remoteMessage.data[APP_ID] == BuildConfig.APP_ID && remoteMessage.data[CAFE_ID] == cafeId) {
                withContext(Main) {
                    showNotification()
                }
            }
        }
    }

    private fun showNotification() {
        Log.d("NotificationTag", "showNotification")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_not_accepted)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_TEXT)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    companion object {
        private const val APP_ID = "app_id"
        private const val CAFE_ID = "cafe_id"
        private const val NOTIFICATION_TITLE = "Новый заказ"
        private const val NOTIFICATION_TEXT = "Новый заказ"
    }
}