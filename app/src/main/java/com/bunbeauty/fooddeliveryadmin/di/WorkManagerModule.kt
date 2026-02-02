package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.work.UpdateNotificationTokenInteractor
import org.koin.dsl.module

fun workManagerModule() =
    module {
        single {
            UpdateNotificationTokenInteractor(
                dataStoreRepo = get(),
                foodDeliveryApi = get(),
            )
        }
    }
