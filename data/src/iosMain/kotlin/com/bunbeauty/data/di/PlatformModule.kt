package com.bunbeauty.data.di

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun platformDataModule() =
    module {
        single<HttpClient> { createIosHttpClient(get()) }
        single<DataStoreRepo> { IosDataStoreRepository() }
        single<UserAuthorizationRepo> {
            IosUserAuthorizationRepository(
                networkConnector = get(),
                dataStoreRepo = get(),
            )
        }
        single<PhotoRepo> { IosStubPhotoRepository() }
    }
