package com.bunbeauty.fooddeliveryadmin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import com.bunbeauty.common.Constants.CHANNEL_ID
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

private const val NOTIFICATION_CHANNEL_NAME = "Main channel"

@HiltAndroidApp
class FoodDeliveryAdminApplication : Application(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Default

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
            enableLights(true)
            enableVibration(true)

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            setSound(soundUri, attributes)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
