package com.bunbeauty.fooddeliveryadmin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.bunbeauty.common.Constants
import com.bunbeauty.domain.repo.DataStoreRepo
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class FoodDeliveryAdminApplication : Application(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Default

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    override fun onCreate() {
        super.onCreate()
  /*      dataStoreRepo.companyUuid.onEach { companyUuid ->
            if (companyUuid.isNotEmpty())
                bindService(
                    Intent(this, WebSocketService::class.java),
                    getServiceConnection(), BIND_AUTO_CREATE
                )
        }.launchIn(this)*/
    }

   /* private fun getServiceConnection(): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, localBinder: IBinder) {}
            override fun onServiceDisconnected(p0: ComponentName?) {}
        }
    }*/
}