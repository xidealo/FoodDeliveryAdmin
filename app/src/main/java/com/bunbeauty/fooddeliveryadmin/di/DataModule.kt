package com.bunbeauty.fooddeliveryadmin.di

import android.content.Context
import androidx.room.Room
import com.bunbeauty.data.LocalDatabase
import com.bunbeauty.fooddeliveryadmin.BuildConfig.FB_LINK
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance(FB_LINK)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "EldDatabase"
        ).fallbackToDestructiveMigration().build()

    //DAO

    @Singleton
    @Provides
    fun provideOrderDao(localDatabase: LocalDatabase) = localDatabase.orderDao()

    @Singleton
    @Provides
    fun provideAddressDao(localDatabase: LocalDatabase) = localDatabase.addressDao()

    @Singleton
    @Provides
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()

    @Singleton
    @Provides
    fun provideMenuProductDao(localDatabase: LocalDatabase) = localDatabase.menuProductDao()
}