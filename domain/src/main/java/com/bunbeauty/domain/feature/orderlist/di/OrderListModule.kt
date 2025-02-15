package com.bunbeauty.domain.feature.orderlist.di

import com.bunbeauty.domain.feature.orderlist.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderListFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetSelectedCafeUseCase
import com.bunbeauty.domain.feature.orderlist.SaveSelectedCafeUuidUseCase
import org.koin.dsl.module

fun orderListModule() = module {
    factory {
        CheckIsAnotherCafeSelectedUseCase(
            getSelectedCafe = get()
        )
    }

    factory {
        GetOrderErrorFlowUseCase(
            getSelectedCafe = get(),
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

    factory {
        GetSelectedCafeUseCase(
            getSelectedCafeFlow = get()
        )
    }

    factory {
        SaveSelectedCafeUuidUseCase(
            getSelectedCafe = get(),
            dataStoreRepo = get()
        )
    }
}
