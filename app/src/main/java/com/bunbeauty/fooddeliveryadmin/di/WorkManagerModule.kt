package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.work.UpdateNotificationTokenWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

fun workManagerModule() =
    module {
        worker {
            UpdateNotificationTokenWorker(
                appContext = get(),
                workerParams = get(),
                dataStoreRepo = get(),
                foodDeliveryApi = get(),
            )
        }
    }
