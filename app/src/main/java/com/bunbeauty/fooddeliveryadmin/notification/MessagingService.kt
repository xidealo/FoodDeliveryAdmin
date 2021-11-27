package com.bunbeauty.fooddeliveryadmin.notification

/*
@AndroidEntryPoint
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : CoroutineScope {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepo

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val coroutineContext = Job()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NotificationTag", "onMessageReceived")

        launch(IO) {
            val cafeId = dataStoreRepo.cafeUuid.first()
            if (remoteMessage.data[APP_ID_KEY] == "PAPA_KARLO" &&
                remoteMessage.data[CAFE_ID_KEY] == cafeId
            ) {
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
            .setSmallIcon(R.drawable.ic_new_order)
            .setContentTitle(resourcesProvider.getString(R.string.title_messaging_new_order))
            .setContentText(resourcesProvider.getString(R.string.msg_messaging_new_order))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setColor(resourcesProvider.getColor(R.color.lightIconColor))
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }
}*/
