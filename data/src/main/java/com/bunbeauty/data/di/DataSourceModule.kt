package com.bunbeauty.data.di

import android.content.Context
import androidx.room.Room
import com.bunbeauty.data.BuildConfig
import com.bunbeauty.data.LocalDatabase
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
class DataSourceModule {

    // FIREBASE

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.FB_LINK)

    @Singleton
    @Provides
    fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference.child("PAPA_KARLO")

    // LOCAL DATABASE

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "EldDatabase"
        ).fallbackToDestructiveMigration().build()

    // DAO

    @Singleton
    @Provides
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()
}