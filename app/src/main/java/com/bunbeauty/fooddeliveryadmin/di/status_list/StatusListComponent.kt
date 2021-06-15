package com.bunbeauty.fooddeliveryadmin.di.status_list

import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheet
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        StatusListBinding::class,
        StatusListModule::class
    ]
)
interface StatusListComponent {

    fun inject(fragment: StatusListBottomSheet)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: StatusListBottomSheet): StatusListComponent
    }
}