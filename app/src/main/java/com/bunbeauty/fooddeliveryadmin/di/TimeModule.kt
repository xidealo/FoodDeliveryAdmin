package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.domain.feature.time.TimeService
import com.bunbeauty.fooddeliveryadmin.time.KotlinXDateTimeService
import org.koin.dsl.module

fun timeModule() =
    module {
        single<TimeService> {
            KotlinXDateTimeService()
        }
    }
