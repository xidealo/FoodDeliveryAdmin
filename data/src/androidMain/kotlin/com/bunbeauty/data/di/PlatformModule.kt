package com.bunbeauty.data.di

import android.util.Log
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import com.bunbeauty.data.BuildConfig
import com.bunbeauty.data.repository.DataStoreRepository
import com.bunbeauty.data.repository.PhotoRepository
import com.bunbeauty.data.repository.UserAuthorizationRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private const val YC_ENDPOINT = "https://storage.yandexcloud.net"
private const val YC_REGION = "ru-central1"

actual fun platformDataModule() =
    module {
        single<DataStoreRepo> {
            DataStoreRepository(
                context = get(),
            )
        }
        single<UserAuthorizationRepo> {
            UserAuthorizationRepository(
                context = get(),
                dataStoreRepo = get(),
                networkConnector = get(),
            )
        }
        single<PhotoRepo> {
            PhotoRepository(
                context = get(),
                s3Client = get(),
                bucket = BuildConfig.YC_BUCKET,
            )
        }

        single {
            S3Client {
                region = YC_REGION
                endpointUrl = Url.parse(YC_ENDPOINT)
                forcePathStyle = false
                credentialsProvider =
                    StaticCredentialsProvider {
                        accessKeyId = BuildConfig.YC_ACCESS_KEY
                        secretAccessKey = BuildConfig.YC_SECRET_KEY
                    }
            }
        }

        single {
            HttpClient(OkHttp.create()) {
                install(ContentNegotiation) {
                    json(get<Json>())
                }
                install(WebSockets) {
                    pingInterval = 20.seconds
                }
                install(Logging) {
                    logger =
                        object : Logger {
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
                    host = "fooddelivery-xidealo.amvera.io"
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    contentType(ContentType.Application.Json)

                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }
            }
        }
    }
