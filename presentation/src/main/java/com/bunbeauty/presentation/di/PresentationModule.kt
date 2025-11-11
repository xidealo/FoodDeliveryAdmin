package com.bunbeauty.presentation.di
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.utils.ResourcesProvider
import com.bunbeauty.presentation.utils.StringUtil
import org.koin.dsl.module

fun presentationModule() =
    module {
        single<IStringUtil> {
            StringUtil(
                dateTimeUtil = get(),
                resources = get(),
                resourcesProvider = get(),
            )
        }

        single<IResourcesProvider> {
            ResourcesProvider(
                context = get(),
            )
        }
    }
