package com.bunbeauty.fooddeliveryadmin.di.components

import androidx.lifecycle.ViewModelStoreOwner
import com.bunbeauty.fooddeliveryadmin.ui.activities.MainActivity
import com.bunbeauty.fooddeliveryadmin.di.modules.ViewModelModule
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.AddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu.CreateNewMenuProductFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu.EditMenuFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.login.LoginFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu.EditMenuProductFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.SelectedStatisticBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticAddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticPeriodBottomSheet

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
    fun inject(editMenuFragment: EditMenuFragment)
    fun inject(editMenuProductFragment: EditMenuProductFragment)
    fun inject(createNewMenuProductFragment: CreateNewMenuProductFragment)

    // bottom sheet
    fun inject(selectedStatisticBottomSheet: SelectedStatisticBottomSheet)
    fun inject(addressListBottomSheet: AddressListBottomSheet)
    fun inject(statisticAddressListBottomSheet: StatisticAddressListBottomSheet)
    fun inject(statisticPeriodBottomSheet: StatisticPeriodBottomSheet)
    fun inject(ordersDetailsFragment: OrdersDetailsFragment)
}