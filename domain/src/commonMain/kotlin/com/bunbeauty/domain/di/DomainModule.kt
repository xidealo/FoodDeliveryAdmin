package com.bunbeauty.domain.di

import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.domain.util.product.ProductUtil
import org.koin.dsl.module

fun domainModule() =
    module {
        single<IProductUtil> { ProductUtil() }
    }
