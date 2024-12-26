package com.bunbeauty.domain.feature.profile.di

import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.feature.profile.IsOrderAvailableUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.UpdateOrderAvailabilityUseCase
import org.koin.dsl.module

fun profileModule() = module {
    factory {
        GetIsUnlimitedNotificationUseCase(
            settingsRepo = get()
        )
    }

    factory {
        GetUsernameUseCase(
            dataStoreRepo = get()
        )
    }

    factory {
        IsOrderAvailableUseCase(
            dataStoreRepo = get(),
            orderRepo = get()
        )
    }

    factory {
        UpdateIsUnlimitedNotificationUseCase(
            settingsRepo = get()
        )
    }

    factory {
        UpdateOrderAvailabilityUseCase(
            dataStoreRepo = get(),
            orderRepo = get()
        )
    }
}
