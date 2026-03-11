package com.bunbeauty.presentation.di

import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.utils.StringUtil
import org.koin.dsl.module

fun presentationModule() =
    module {
        single<IStringUtil> {
            StringUtil(
                dateTimeUtil = get(),
            )
        }
    }
