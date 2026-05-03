package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.shared.feature.orderlist.state.OrderMapper
import org.koin.dsl.module

fun uiMapperModule() =
    module {
        factory {
            OrderMapper()
        }
    }
