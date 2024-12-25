package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.work.UpdateNotificationTokenWorker
import org.koin.dsl.module

fun workManagerModule() = module {
    factory {
        UpdateNotificationTokenWorker(
            appContext = get(),
            workerParams = get(),
            dataStoreRepo = get(),
            foodDeliveryApi = get()
        )
    }
}
