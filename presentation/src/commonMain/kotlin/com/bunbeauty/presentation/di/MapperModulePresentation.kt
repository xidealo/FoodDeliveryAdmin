package com.bunbeauty.presentation.di

import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import org.koin.dsl.module

fun presentationMapperModule() =
    module {
        single {
            OrderStatusMapper(
                resources = get(),
            )
        }
    }
