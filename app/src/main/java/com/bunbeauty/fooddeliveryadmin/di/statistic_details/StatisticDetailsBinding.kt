package com.bunbeauty.fooddeliveryadmin.di.statistic_details

import androidx.lifecycle.ViewModel
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModelImpl
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticDetailsViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StatisticDetailsBinding {

    @Binds
    @IntoMap
    @ViewModelKey(StatisticDetailsViewModel::class)
    abstract fun bindStatisticDetailsViewModel(viewModel: StatisticDetailsViewModelImpl): ViewModel
}