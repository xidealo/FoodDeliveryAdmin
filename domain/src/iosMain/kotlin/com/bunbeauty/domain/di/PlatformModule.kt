package com.bunbeauty.domain.di

import com.bunbeauty.domain.platform.UuidGenerator
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun platformModule() =
    module {
        single {
            UuidGenerator()
        }
    }
