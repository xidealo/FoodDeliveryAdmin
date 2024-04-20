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
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import io.ktor.client.plugins.logging.Logger as KtorLogger

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

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
    fun provideKtorHttpClient() = HttpClient(OkHttp.create()) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }
        install(WebSockets)
        install(Logging) {
            logger = object : KtorLogger {
                override fun log(message: String) {
                    Log.v("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        install(DefaultRequest) {
            host = "food-delivery-api-bunbeauty.herokuapp.com"
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            url {
                protocol = URLProtocol.HTTPS
            }
        }
    }

    // DAO

    @Singleton
    @Provides
    fun provideCafeDao(localDatabase: LocalDatabase) = localDatabase.cafeDao()

    @Singleton
    @Provides
    fun provideMenuProductDao(localDatabase: LocalDatabase) = localDatabase.menuProductDao()

    @Singleton
    @Provides
    fun provideCategoryDao(localDatabase: LocalDatabase) = localDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideMenuProductCategoryDao(localDatabase: LocalDatabase) =
        localDatabase.menuProductCategoryDao()

    @Singleton
    @Provides
    fun provideCityDao(localDatabase: LocalDatabase) = localDatabase.cityDao()

    @Singleton
    @Provides
    fun provideNonWorkingDayDao(localDatabase: LocalDatabase) = localDatabase.nonWorkingDayDao()
}
