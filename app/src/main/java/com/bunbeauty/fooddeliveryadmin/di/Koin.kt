package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.di.dataSourceModule
import com.bunbeauty.data.di.mapperModule
import com.bunbeauty.data.di.repositoryModule
import com.bunbeauty.domain.di.domainModule
import com.bunbeauty.domain.feature.additiongrouplist.di.additionGroupListModule
import com.bunbeauty.domain.feature.additionlist.di.additionListModule
import com.bunbeauty.domain.feature.cafelist.di.cafeListModule
import com.bunbeauty.domain.feature.editcafe.di.editCafeModule
import com.bunbeauty.domain.feature.gallery.di.galleryModule
import com.bunbeauty.domain.feature.login.di.loginModule
import com.bunbeauty.domain.feature.main.di.mainModule
import com.bunbeauty.domain.feature.orderlist.di.orderListModule
import com.bunbeauty.domain.feature.profile.di.profileModule
import com.bunbeauty.domain.feature.time.di.timeUseCaseModule
import com.bunbeauty.presentation.di.presentationMapperModule
import com.bunbeauty.presentation.di.presentationModule
import com.bunbeauty.presentation.di.presentationViewModelModule
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    workManagerFactory()
    modules(
        appModule(),
        uiMapperModule(),
        domainModule(),
        useCaseModule(),
        editCafeModule(),
        additionGroupListModule(),
        additionListModule(),
        galleryModule(),
        loginModule(),
        mainModule(),
        orderListModule(),
        profileModule(),
        timeUseCaseModule(),
        viewModelModule(),
        workManagerModule(),
        timeModule(),
        presentationModule(),
        presentationMapperModule(),
        presentationViewModelModule(),
        repositoryModule(),
        mapperModule(),
        dataSourceModule(),
        cafeListModule()
    )
}
