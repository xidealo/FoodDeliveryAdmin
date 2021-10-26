package com.bunbeauty.fooddeliveryadmin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.bunbeauty.common.Constants
import com.bunbeauty.domain.repo.DataStoreRepo
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class FoodDeliveryAdminApplication : Application(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Default

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this);
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}