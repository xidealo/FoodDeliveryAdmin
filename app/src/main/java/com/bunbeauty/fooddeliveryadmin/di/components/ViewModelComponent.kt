package com.bunbeauty.fooddeliveryadmin.di.components

import androidx.lifecycle.ViewModelStoreOwner
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.fooddeliveryadmin.di.modules.ViewModelModule
import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginFragment
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragment

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance viewModelStoreOwner: ViewModelStoreOwner): ViewModelComponent
    }

    // activities
    fun inject(mainActivity: MainActivity)

    // fragments
    fun inject(loginFragment: LoginFragment)
    fun inject(ordersFragment: OrdersFragment)

    // dialogs
}