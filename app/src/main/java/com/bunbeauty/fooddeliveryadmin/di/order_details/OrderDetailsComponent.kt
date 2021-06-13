package com.bunbeauty.fooddeliveryadmin.di.order_details

import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        OrderDetailsBinding::class,
        OrderDetailsModule::class
    ]
)
interface OrderDetailsComponent {

    fun inject(fragment: OrderDetailsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: OrderDetailsFragment): OrderDetailsComponent
    }
}