package com.bunbeauty.fooddeliveryadmin.di.order_details

import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs.fromBundle
import dagger.Module
import dagger.Provides

@Module
class OrderDetailsModule {

    @Provides
    fun provideOrderDetailsArgs(fragment: OrderDetailsFragment) =
        fromBundle(fragment.requireArguments())
}