package com.bunbeauty.fooddeliveryadmin.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.ADMIN_SERVICE_CHANNEL_ID
import com.bunbeauty.common.Constants.ADMIN_SERVICE_CHANNEL_NAME
import com.bunbeauty.common.Constants.ADMIN_SERVICE_ID
import com.bunbeauty.common.Constants.CHANNEL_ID
import com.bunbeauty.common.Constants.NOTIFICATION_ID
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.ui.MainActivity
import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.utils.IResourcesProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class NotificationService : LifecycleService(), CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    @Inject
    lateinit var orderRepo: OrderRepo

    override fun onCreate() {
        super.onCreate()
        startForeground(ADMIN_SERVICE_ID, createNotification())

        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            dataStoreRepo.token.onEach { token ->
                if (cafeId.isNotEmpty()) {
                    orderRepo.unsubscribeOnOrderList()
                    orderRepo.subscribeOnOrderListByCafeId(token, cafeId)
                }
            }
        }.launchIn(this)


        orderRepo.newOrder.onEach { order ->
            withContext(Dispatchers.Main) {
                showNotification(order)
            }
        }.launchIn(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //disconnect()
        return super.onUnbind(intent)
    }

    private fun createNotification(): Notification {
        val serviceChannel = NotificationChannel(
            ADMIN_SERVICE_CHANNEL_ID,
            ADMIN_SERVICE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(serviceChannel)
        return Notification.Builder(this, ADMIN_SERVICE_CHANNEL_ID)
            .setContentTitle(resourcesProvider.getString(R.string.title_notification))
            .setContentText(resourcesProvider.getString(R.string.msg_notification_info))
            .setSmallIcon(R.drawable.ic_visible)
            .build()
    }

    private fun showNotification(order: Order) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_new_order)
            .setContentTitle("${resourcesProvider.getString(R.string.title_messaging_new_order)} ${order.code}")
            .setContentText(resourcesProvider.getString(R.string.msg_messaging_new_order))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setColor(resourcesProvider.getColor(R.color.lightIconColor))
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }
}