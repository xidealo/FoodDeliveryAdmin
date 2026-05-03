package com.bunbeauty.data.di

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
    }
