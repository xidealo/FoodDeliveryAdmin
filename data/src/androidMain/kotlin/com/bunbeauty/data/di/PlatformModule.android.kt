package com.bunbeauty.data.di

import com.bunbeauty.data.repository.AndroidUserAuthorizationRepository
import com.bunbeauty.data.repository.DataStoreRepository
import com.bunbeauty.data.repository.PhotoRepository
import com.bunbeauty.data.work.UpdateNotificationTokenWorker
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

actual fun platformDataModule() =
    module {
        single<DataStoreRepo> {
            DataStoreRepository(
                context = get(),
            )
        }
        single<UserAuthorizationRepo> {
            AndroidUserAuthorizationRepository(
                networkConnector = get(),
                dataStoreRepo = get(),
                context = get(),
            )
        }
        worker {
            UpdateNotificationTokenWorker(
                appContext = get(),
                workerParams = get(),
                dataStoreRepo = get(),
                foodDeliveryApi = get(),
            )
        }
        single<PhotoRepo> {
            PhotoRepository(
                context = get(),
            )
        }
    }
