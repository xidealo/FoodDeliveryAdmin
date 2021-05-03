package com.bunbeauty.fooddeliveryadmin.di.modules

import android.content.Context
import androidx.room.Room
import com.bunbeauty.domain.LocalDatabase
import com.bunbeauty.fooddeliveryadmin.BuildConfig.FB_LINK
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance(FB_LINK)

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

    @Provides
    fun provideMenuProductDao(localDatabase: LocalDatabase) = localDatabase.menuProductDao()
}