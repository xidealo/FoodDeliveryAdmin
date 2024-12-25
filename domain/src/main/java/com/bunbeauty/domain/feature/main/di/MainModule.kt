package com.bunbeauty.domain.feature.main.di

import com.bunbeauty.domain.feature.main.GetIsNonWorkingDayFlowUseCase
import com.bunbeauty.domain.feature.main.GetSelectedCafeFlowUseCase
import org.koin.dsl.module

fun mainModule() = module {
    factory {
        GetIsNonWorkingDayFlowUseCase(
            getSelectedCafeFlow = get(),
            getCurrentTimeMillisFlow = get(),
            nonWorkingDayRepo = get()
        )
    }

    factory {
        GetSelectedCafeFlowUseCase(
            dataStoreRepo = get(),
            cafeRepo = get()
        )
    }
}