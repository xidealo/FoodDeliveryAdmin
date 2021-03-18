package com.bunbeauty.fooddeliveryadmin.di.modules

import android.content.Context
import androidx.room.Room
import com.bunbeauty.domain.LocalDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "EldDatabase"
    ).fallbackToDestructiveMigration().build()

    //DAO

    @Provides
    fun provideOrderDao(localDatabase: LocalDatabase) = localDatabase.orderDao()

    @Provides
    fun provideAddressDao(localDatabase: LocalDatabase) = localDatabase.addressDao()

    @Provides
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()
}