package com.bunbeauty.domain.feature.cafelist.di

import com.bunbeauty.domain.feature.cafelist.GetCafeWithWorkingHoursFlowUseCase
import com.bunbeauty.domain.feature.cafelist.GetTimeZoneByCityUuidUseCase
import org.koin.dsl.module

fun cafeListModule() =
    module {
        factory {
            GetCafeWithWorkingHoursFlowUseCase(
                dateTimeUtil = get(),
                getCafe = get(),
                getCurrentTimeFlow = get(),
            )
        }

        factory {
            GetTimeZoneByCityUuidUseCase(
                dataStoreRepo = get(),
                cityRepo = get(),
            )
        }
    }
