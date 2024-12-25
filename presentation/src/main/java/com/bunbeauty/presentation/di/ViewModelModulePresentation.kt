package com.bunbeauty.presentation.di

import com.bunbeauty.presentation.feature.menu.MenuViewModel
import org.koin.dsl.module


fun presentationViewModelModule() = module {
    single {
        MenuViewModel()
    }
}