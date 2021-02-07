package com.bunbeauty.fooddeliveryadmin.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeliveryadmin.view_model.*
import com.bunbeauty.fooddeliveryadmin.view_model.base.ViewModelFactory
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
}