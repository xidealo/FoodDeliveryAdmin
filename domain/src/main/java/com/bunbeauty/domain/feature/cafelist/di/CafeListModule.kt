package com.bunbeauty.domain.feature.cafelist.di

import com.bunbeauty.domain.feature.cafelist.GetCafeWithWorkingHoursListFlowUseCase
import com.bunbeauty.domain.feature.cafelist.GetTimeZoneByCityUuidUseCase
import org.koin.dsl.module

fun cafeListModule() = module {
    factory {
        GetCafeWithWorkingHoursListFlowUseCase(
            dateTimeUtil = get(),
            getCafeList = get(),
            getCurrentTimeFlow = get()
        )
    }

    factory {
        GetTimeZoneByCityUuidUseCase(
            dataStoreRepo = get(),
            cityRepo = get()
        )
    }
}
