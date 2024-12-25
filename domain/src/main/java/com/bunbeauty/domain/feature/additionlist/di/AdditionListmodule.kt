package com.bunbeauty.domain.feature.additionlist.di

import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import org.koin.dsl.module

fun additionListModule() = module {
    factory {
        GetSeparatedAdditionListUseCase(
            dataStoreRepo = get(),
            additionRepo = get(),
            getNewUuidUseCase = get()
        )
    }

    factory {
        UpdateVisibleAdditionUseCase(
            dataStoreRepo = get(),
            additionRepo = get()
        )
    }
}
