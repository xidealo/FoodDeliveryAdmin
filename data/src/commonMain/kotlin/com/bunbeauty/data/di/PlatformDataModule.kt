package com.bunbeauty.data.di

import org.koin.core.module.Module
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
expect fun platformDataModule(): Module
