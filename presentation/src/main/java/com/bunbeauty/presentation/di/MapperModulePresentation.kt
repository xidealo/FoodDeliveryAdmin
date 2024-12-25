package com.bunbeauty.presentation.di

import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import com.bunbeauty.presentation.feature.orderlist.mapper.OrderMapper
import org.koin.dsl.module

fun presentationMapperModule() = module {
    single {
        OrderMapper(
            orderStatusMapper = get(),
            dateTimeUtil = get(),
            resources = get()
        )
    }

    single {
        OrderStatusMapper(
            resources = get()
        )
    }
}