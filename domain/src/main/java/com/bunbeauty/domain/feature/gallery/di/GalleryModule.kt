package com.bunbeauty.domain.feature.gallery.di

import com.bunbeauty.domain.feature.gallery.FetchPhotoListUseCase
import com.bunbeauty.domain.feature.gallery.GetPhotoListUseCase
import org.koin.dsl.module

fun galleryModule() = module {
    factory {
        FetchPhotoListUseCase(
            getUsernameUseCase = get(),
            photoRepo = get()
        )
    }

    factory {
        GetPhotoListUseCase(
            getUsernameUseCase = get(),
            photoRepo = get()
        )
    }
}
