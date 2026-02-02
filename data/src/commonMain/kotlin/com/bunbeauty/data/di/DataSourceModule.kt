package com.bunbeauty.data.di

import com.bunbeauty.data.network.createHttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun dataSourceModule() =
    module {
        single {
            Json {
                isLenient = false
                ignoreUnknownKeys = true
            }
        }

        single {
            createHttpClient()
        }
    }
