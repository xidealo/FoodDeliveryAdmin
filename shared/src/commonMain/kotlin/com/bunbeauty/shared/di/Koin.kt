package com.bunbeauty.shared.di

import com.bunbeauty.data.di.dataSourceModule
import com.bunbeauty.data.di.mapperModule
import com.bunbeauty.data.di.platformDataModule
import com.bunbeauty.data.di.repositoryModule
import com.bunbeauty.domain.di.domainModule
import com.bunbeauty.domain.di.platformModule
import com.bunbeauty.domain.feature.main.di.mainModule
import com.bunbeauty.domain.feature.time.di.timeUseCaseModule
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
            repositoryModule(),
            mapperModule(),
            timeUseCaseModule(),
            mainModule(),
            presentationModule(),
            viewModelModule(),
        )
    }

fun doInitKoin() {
    initKoin()
}

