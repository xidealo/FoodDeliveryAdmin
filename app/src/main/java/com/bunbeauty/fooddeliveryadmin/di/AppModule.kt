package com.bunbeauty.fooddeliveryadmin.di

import android.content.res.Resources
import androidx.core.app.NotificationManagerCompat
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun appModule() =
    module {
        single<Resources> {
            androidContext().resources
        }

        single {
            NotificationManagerCompat.from(androidContext())
        }
    }
