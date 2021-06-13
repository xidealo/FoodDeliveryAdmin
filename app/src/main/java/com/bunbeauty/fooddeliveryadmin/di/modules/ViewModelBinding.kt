package com.bunbeauty.fooddeliveryadmin.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeliveryadmin.presentation.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBinding {

    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EmptyViewModel::class)
    internal abstract fun provideEmptyViewModel(emptyViewModel: EmptyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun provideLoginViewModel(loginViewModel: LoginViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    internal abstract fun provideOrdersViewModel(ordersViewModelImpl: OrdersViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    internal abstract fun provideStatisticViewModel(statisticViewModel: StatisticViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedStatisticViewModel::class)
    internal abstract fun provideSelectedStatisticViewModel(selectedStatisticViewModel: SelectedStatisticViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressListViewModel::class)
    internal abstract fun provideAddressListViewModel(addressListViewModel: AddressListViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditMenuViewModel::class)
    internal abstract fun provideEditMenuViewModel(editMenuViewModel: EditMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditMenuProductViewModel::class)
    internal abstract fun provideEditMenuProductViewModel(editMenuProductViewModel: EditMenuProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateNewMenuProductViewModel::class)
    internal abstract fun provideCreateNewMenuProductViewModel(createNewMenuProductViewModel: CreateNewMenuProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticAddressListViewModel::class)
    internal abstract fun provideStatisticAddressListViewModel(statisticAddressListViewModel: StatisticAddressListViewModelImpl): ViewModel
}