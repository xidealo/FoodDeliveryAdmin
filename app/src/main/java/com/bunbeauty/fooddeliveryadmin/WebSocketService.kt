package com.bunbeauty.fooddeliveryadmin

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.lifecycle.LifecycleService
import com.bunbeauty.common.Constants.ADMIN_SERVICE_CHANNEL_ID
import com.bunbeauty.common.Constants.ADMIN_SERVICE_CHANNEL_NAME
import com.bunbeauty.common.Constants.ADMIN_SERVICE_ID
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.utils.IResourcesProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketService : LifecycleService(), CoroutineScope {

    //NOT NEED

    override val coroutineContext = SupervisorJob()

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    @Inject
    lateinit var orderRepo: OrderRepo

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo


    override fun onCreate() {
        super.onCreate()
        startForeground(ADMIN_SERVICE_ID, createNotification())
        startWebSocket()
    }

    fun startWebSocket() {
        dataStoreRepo.token.flatMapLatest { token ->
            dataStoreRepo.cafeUuid.onEach { cafeId ->
                if (cafeId.isNotEmpty()) {
                    //orderRepo.subscribeOnOrderListByCafeId(token, cafeId)
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun createNotification(): Notification {
        val serviceChannel = NotificationChannel(
            ADMIN_SERVICE_CHANNEL_ID,
            ADMIN_SERVICE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_MAX
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

}