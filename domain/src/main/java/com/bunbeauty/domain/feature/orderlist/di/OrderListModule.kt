package com.bunbeauty.domain.feature.orderlist.di

import com.bunbeauty.domain.feature.orderlist.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderListFlowUseCase
import org.koin.dsl.module

fun orderListModule() = module {
    factory {
        GetOrderErrorFlowUseCase(
            dataStoreRepo = get(),
            orderRepository = get()
        )
    }

    factory {
        GetOrderListFlowUseCase(
            dataStoreRepo = get(),
            orderRepo = get()
        )
    }
}
