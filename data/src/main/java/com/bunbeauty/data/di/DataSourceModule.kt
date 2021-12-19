package com.bunbeauty.data.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.bunbeauty.data.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    // FIREBASE

    /*  @Singleton
      @Provides
      fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.FB_LINK)

      @Singleton
      @Provides
      fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference.child("PAPA_KARLO")
  */
    // LOCAL DATABASE

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "EldDatabase"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        isLenient = false
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideKtorHttpClient(json: Json) = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(WebSockets)

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            host = "food-delivery-api-bunbeauty.herokuapp.com"
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }


    // DAO

    @Singleton
    @Provides
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()

    @Singleton
    @Provides
    fun provideMenuProductDao(localDatabase: LocalDatabase) = localDatabase.menuProductDao()
}