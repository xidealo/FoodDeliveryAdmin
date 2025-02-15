package com.bunbeauty.domain.feature.login.di

import com.bunbeauty.domain.feature.login.CheckAuthorizationUseCase
import com.bunbeauty.domain.feature.login.LoginUseCase
import org.koin.dsl.module

fun loginModule() = module {
    factory {
        CheckAuthorizationUseCase(
            userAuthorizationRepo = get(),
            dataStoreRepo = get()
        )
    }

    factory {
        LoginUseCase(
            userAuthorizationRepo = get(),
            dataStoreRepo = get()
        )
    }
}
