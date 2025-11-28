package com.bunbeauty.domain.feature.main.di

import com.bunbeauty.domain.feature.main.GetIsNonWorkingDayFlowUseCase
import org.koin.dsl.module

fun mainModule() = module {
    factory {
        GetIsNonWorkingDayFlowUseCase(
            getCurrentTimeMillisFlow = get(),
            nonWorkingDayRepo = get(),
            getCafeUseCase = get()
        )
    }
}
