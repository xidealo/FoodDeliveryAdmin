package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.domain.feature.time.TimeService
import com.bunbeauty.fooddeliveryadmin.time.KotlinXDateTimeService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TimeModule {

    @Singleton
    @Binds
    fun bindTimeService(kotlinXDateTimeService: KotlinXDateTimeService): TimeService
}
