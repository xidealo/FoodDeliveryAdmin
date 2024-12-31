package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.fooddeliveryadmin.screen.cafelist.CafeStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderDetailsStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderProductMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.PaymentMethodMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.OrderMapper
import org.koin.dsl.module

fun uiMapperModule() = module {
    factory {
        CafeStateMapper(resources = get())
    }

    factory {
        OrderDetailsStateMapper(
            dateTimeUtil = get(),
            orderStatusMapper = get(),
            orderProductMapper = get(),
            paymentMethodMapper = get()
        )
    }

    factory {
        OrderMapper(
            orderStatusMapper = get(),
            dateTimeUtil = get(),
            resources = get()
        )
    }

    factory { OrderProductMapper() }

    factory { PaymentMethodMapper(resources = get()) }
}
