package com.bunbeauty.data.network
import com.example.core.motivation.Logger
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger as KtorLogger

actual fun createHttpClient(): HttpClient =
    HttpClient(OkHttp) {
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
                        Logger.logD("Ktor", message)
                    }
                }
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "food-delivery-api-bunbeauty.herokuapp.com"
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
