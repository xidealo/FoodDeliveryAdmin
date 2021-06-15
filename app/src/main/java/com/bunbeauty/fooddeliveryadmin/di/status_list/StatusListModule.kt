package com.bunbeauty.fooddeliveryadmin.di.status_list

import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheetArgs.fromBundle
import dagger.Module
import dagger.Provides

@Module
class StatusListModule {

    @Provides
    fun provideStatusListArgs(fragment: StatusListBottomSheet) =
        fromBundle(fragment.requireArguments())
}