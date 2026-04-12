package com.bunbeauty.data.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
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
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal fun createIosHttpClient(json: Json): HttpClient =
    HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(json)
        }
        install(WebSockets)
        install(Logging) {
            logger =
                object : KtorLogger {
                    override fun log(message: String) {
                        println("NETWORK MESSAGE: $message")
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
