package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.fooddeliveryadmin.screen.cafelist.CafeStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderDetailsStateMapper
import org.koin.dsl.module

fun uiMapperModule() = module {
    factory {
        CafeStateMapper(resources = get())
    }

    factory {
        OrderDetailsStateMapper(
            resources = get(),
            dateTimeUtil = get(),
            orderStatusMapper = get(),
            orderProductMapper = get(),
            paymentMethodMapper = get()
        )
    }
}
