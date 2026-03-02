package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderDetailsStateMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.OrderProductMapper
import com.bunbeauty.fooddeliveryadmin.screen.orderdetails.PaymentMethodMapper
import com.bunbeauty.presentation.feature.orderlist.state.OrderMapper
import org.koin.dsl.module

fun uiMapperModule() =
    module {
        factory {
            OrderDetailsStateMapper(
                dateTimeUtil = get(),
                orderStatusMapper = get(),
                orderProductMapper = get(),
                paymentMethodMapper = get(),
            )
        }

        factory {
            OrderMapper(
                dateTimeUtil = get(),
            )
        }

        factory { OrderProductMapper() }

        factory { PaymentMethodMapper(resources = get()) }
    }
