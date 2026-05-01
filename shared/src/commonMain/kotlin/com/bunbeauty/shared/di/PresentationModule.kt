package com.bunbeauty.shared.di

import DateTimeUtil
import com.bunbeauty.shared.utils.IStringUtil
import com.bunbeauty.shared.utils.StringUtil
import org.koin.dsl.module

fun presentationModule() =
    module {
        single { DateTimeUtil }
        single<IStringUtil> {
            StringUtil(
                dateTimeUtil = get(),
            )
        }
    }
