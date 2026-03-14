package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.presentation.feature.orderlist.state.OrderMapper
import org.koin.dsl.module

fun uiMapperModule() =
    module {

        factory {
            OrderMapper(
                dateTimeUtil = get(),
            )
        }
    }
