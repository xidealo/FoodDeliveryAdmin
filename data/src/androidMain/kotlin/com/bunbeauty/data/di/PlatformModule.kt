package com.bunbeauty.data.di

import com.bunbeauty.data.repository.DataStoreRepository
import com.bunbeauty.data.repository.PhotoRepository
import com.bunbeauty.data.repository.UserAuthorizationRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import org.koin.dsl.module

actual fun platformDataModule() =
    module {
        single<DataStoreRepo> {
            DataStoreRepository(
                context = get(),
            )
        }
        single<UserAuthorizationRepo> {
            UserAuthorizationRepository(
                context = get(),
                dataStoreRepo = get(),
                networkConnector = get(),
            )
        }
        single<PhotoRepo> {
            PhotoRepository(
                context = get(),
            )
        }
    }
