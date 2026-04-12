package com.bunbeauty.shared.di

import com.bunbeauty.data.di.dataSourceModule
import com.bunbeauty.data.di.mapperModule
import com.bunbeauty.data.di.platformDataModule
import com.bunbeauty.data.di.repositoryModule
import com.bunbeauty.domain.di.domainModule
import com.bunbeauty.domain.di.platformModule
import com.bunbeauty.domain.feature.additiongrouplist.di.additionGroupListModule
import com.bunbeauty.domain.feature.additionlist.di.additionModule
import com.bunbeauty.domain.feature.gallery.di.galleryModule
import com.bunbeauty.domain.feature.login.di.loginModule
import com.bunbeauty.domain.feature.main.di.mainModule
import com.bunbeauty.domain.feature.mapzonedelivery.di.mapDeliveryArea
import com.bunbeauty.domain.feature.orderlist.di.orderListModule
import com.bunbeauty.domain.feature.profile.di.profileModule
import com.bunbeauty.domain.feature.time.di.timeUseCaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            domainModule(),
            useCaseModule(),
            additionGroupListModule(),
            additionModule(),
            galleryModule(),
            loginModule(),
            mainModule(),
            orderListModule(),
            profileModule(),
            timeUseCaseModule(),
            viewModelModule(),
            presentationModule(),
            repositoryModule(),
            mapperModule(),
            dataSourceModule(),
            mapDeliveryArea(),
            platformModule(),
            platformDataModule(),
            uiMapperModule()
        )
    }

fun doInitKoin() {
    initKoin()
}

