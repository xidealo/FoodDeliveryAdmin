package com.bunbeauty.domain.feature.orderlist.di

import com.bunbeauty.domain.feature.orderlist.ObserveOrderListStreamUseCase
import com.bunbeauty.domain.feature.orderlist.UnsubscribeOrderUpdatesUseCase
import org.koin.dsl.module

fun orderListModule() =
    module {
        factory {
            ObserveOrderListStreamUseCase(
                dataStoreRepo = get(),
                orderRepo = get(),
            )
        }

        factory {
            UnsubscribeOrderUpdatesUseCase(
                orderRepo = get(),
            )
        }
    }
