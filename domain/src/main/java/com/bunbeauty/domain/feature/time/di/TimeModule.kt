package com.bunbeauty.domain.feature.time.di

import com.bunbeauty.domain.feature.time.GetCurrentTimeFlowUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeMillisFlowUseCase
import org.koin.dsl.module

fun timeUseCaseModule() = module {
    factory {
        GetCurrentTimeFlowUseCase(
            timeService = get()
        )
    }

    factory {
        GetCurrentTimeMillisFlowUseCase(
            timeService = get()
        )
    }
}
