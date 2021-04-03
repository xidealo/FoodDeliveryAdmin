package com.bunbeauty.fooddeliveryadmin.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.presentation.view_model.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    internal abstract fun provideOrdersViewModel(ordersViewModel: OrdersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeStatusViewModel::class)
    internal abstract fun provideChangeStatusViewModel(changeStatusViewModel: ChangeStatusViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    internal abstract fun provideStatisticViewModel(statisticViewModel: StatisticViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedStatisticViewModel::class)
    internal abstract fun provideSelectedStatisticViewModel(selectedStatisticViewModel: SelectedStatisticViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressListViewModel::class)
    internal abstract fun provideAddressListViewModel(addressListViewModel: AddressListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditMenuViewModel::class)
    internal abstract fun provideEditMenuViewModel(editMenuViewModel: EditMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditMenuProductViewModel::class)
    internal abstract fun provideEditMenuProductViewModel(editMenuProductViewModel: EditMenuProductViewModel): ViewModel
}