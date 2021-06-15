package com.bunbeauty.fooddeliveryadmin.di.status_list

import androidx.lifecycle.ViewModel
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StatusListBinding {

    @Binds
    @IntoMap
    @ViewModelKey(StatusListViewModel::class)
    abstract fun bindStatusListViewModel(viewModel: StatusListViewModelImpl): ViewModel
}