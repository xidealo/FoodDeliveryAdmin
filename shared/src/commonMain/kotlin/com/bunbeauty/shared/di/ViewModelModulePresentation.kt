package com.bunbeauty.shared.di

import com.bunbeauty.shared.feature.menu.MenuViewModel
import org.koin.dsl.module

fun presentationViewModelModule() =
    module {
        single {
            MenuViewModel()
        }
    }
