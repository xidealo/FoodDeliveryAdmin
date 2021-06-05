package com.bunbeauty.fooddeliveryadmin.di

import android.content.Context
import androidx.room.Room
import com.bunbeauty.data.LocalDatabase
import com.bunbeauty.fooddeliveryadmin.BuildConfig.APP_ID
import com.bunbeauty.fooddeliveryadmin.BuildConfig.FB_LINK
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
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
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FB_LINK)

    @Singleton
    @Provides
    fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference.child(APP_ID)

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
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()
}