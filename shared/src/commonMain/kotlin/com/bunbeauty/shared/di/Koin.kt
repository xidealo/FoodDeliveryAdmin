package com.bunbeauty.shared.di

import com.bunbeauty.data.di.dataSourceModule
import com.bunbeauty.data.di.platformDataModule
import com.bunbeauty.domain.di.domainModule
import com.bunbeauty.domain.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            domainModule(),
            platformModule(),
            dataSourceModule(),
            platformDataModule(),
            presentationModule(),
            presentationViewModelModule(),
        )
    }

fun doInitKoin() {
    initKoin()
}

