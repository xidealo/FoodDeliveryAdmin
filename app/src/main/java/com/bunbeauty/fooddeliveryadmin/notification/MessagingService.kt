package com.bunbeauty.fooddeliveryadmin.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bunbeauty.common.Constants.CHANNEL_ID
import com.bunbeauty.common.Constants.NOTIFICATION_ID
import com.bunbeauty.common.Constants.ORDER_CODE
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.MainActivity
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService(), CoroutineScope {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val coroutineContext = Job()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //Log.d("NotificationTag", "onMessageReceived")

        launch(IO) {
            withContext(Main) {
                showNotification(remoteMessage.data[ORDER_CODE] ?: "")
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(code: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        var pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_new_order)
            .setContentTitle("${resourcesProvider.getString(R.string.title_messaging_new_order)} $code")
            .setContentText(resourcesProvider.getString(R.string.msg_messaging_new_order))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setColor(resourcesProvider.getColor(R.color.lightIconColor))
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }
}
