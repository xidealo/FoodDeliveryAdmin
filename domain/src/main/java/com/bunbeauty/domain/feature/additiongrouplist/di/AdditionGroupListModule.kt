package com.bunbeauty.domain.feature.additiongrouplist.di

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.UpdateVisibleAdditionGroupListUseCase
import org.koin.dsl.module

fun additionGroupListModule() = module {
    factory {
        GetSeparatedAdditionGroupListUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }
    factory {
        UpdateVisibleAdditionGroupListUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }
}