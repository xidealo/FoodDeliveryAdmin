package com.bunbeauty.fooddeliveryadmin.di.modules

import android.content.Context
import androidx.room.Room
import com.bunbeauty.fooddeliveryadmin.data.local.db.LocalDatabase
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
}