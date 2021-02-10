package com.bunbeauty.fooddeliveryadmin.di.components

import androidx.lifecycle.ViewModelStoreOwner
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.fooddeliveryadmin.di.modules.ViewModelModule
import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginFragment
import com.bunbeauty.fooddeliveryadmin.ui.main.MainFragment
import com.bunbeauty.fooddeliveryadmin.ui.orders.ChangeStatusBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragment
import com.bunbeauty.fooddeliveryadmin.ui.statistic.SelectedStatisticBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.statistic.StatisticFragment

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
    fun inject(statisticFragment: StatisticFragment)
    fun inject(mainFragment: MainFragment)

    // bottom sheet
    fun inject(selectedStatisticBottomSheet: SelectedStatisticBottomSheet)

    // dialogs
    fun inject(changeStatusDialog: ChangeStatusBottomSheet)
}