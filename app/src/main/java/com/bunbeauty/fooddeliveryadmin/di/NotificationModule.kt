package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.NotificationService
import com.bunbeauty.fooddeliveryadmin.notification.FirebaseNotificationService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    @Singleton
    @Binds
    fun bindNotificationService(firebaseNotificationService: FirebaseNotificationService): NotificationService

}