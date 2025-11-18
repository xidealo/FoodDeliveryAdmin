package com.bunbeauty.data.di

import android.util.Log
import androidx.room.Room
import com.bunbeauty.data.LocalDatabase
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import io.ktor.client.plugins.logging.Logger as KtorLogger

fun dataSourceModule() =
    module {
        single {
            Room
                .databaseBuilder(
                    androidContext(),
                    LocalDatabase::class.java,
                    "EldDatabase",
                ).fallbackToDestructiveMigration()
                .build()
        }

        single {
            Json {
                isLenient = false
                ignoreUnknownKeys = true
            }
        }

        single {
            HttpClient(OkHttp.create()) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                            encodeDefaults = false
                        },
                    )
                }
                install(WebSockets)
                install(Logging) {
                    logger =
                        object : KtorLogger {
                            override fun log(message: String) {
                                Log.d("Ktor", message)
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
        }

        // DAO

        single {
            get<LocalDatabase>().cityDao()
        }
        single {
            get<LocalDatabase>().nonWorkingDayDao()
        }
    }
