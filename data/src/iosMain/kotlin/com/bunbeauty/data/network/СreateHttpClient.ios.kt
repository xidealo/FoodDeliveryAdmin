package com.bunbeauty.data.network

import io.ktor.client.HttpClient

actual fun createHttpClient(): HttpClient = HttpClient()
