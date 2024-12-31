package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.fooddeliveryadmin.screen.cafelist.CafeStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderDetailsStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderProductMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.PaymentMethodMapper
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

    single { OrderProductMapper() }

    single { PaymentMethodMapper(resources = get()) }
}
