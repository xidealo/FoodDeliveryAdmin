package com.bunbeauty.data.di

import com.bunbeauty.data.repository.IosUserAuthorizationRepository
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import org.koin.dsl.module

actual fun platformDataModule() =
    module {
        single<UserAuthorizationRepo> {
            IosUserAuthorizationRepository(
                networkConnector = get(),
                dataStoreRepo = get(),
            )
        }
        single<PhotoRepo> {
            IosPhotoRepository()
        }
    }
