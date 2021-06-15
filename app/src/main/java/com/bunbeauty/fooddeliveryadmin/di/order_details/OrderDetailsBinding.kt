package com.bunbeauty.fooddeliveryadmin.di.order_details

import androidx.lifecycle.ViewModel
import com.bunbeauty.fooddeliveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OrderDetailsBinding {

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailsViewModel::class)
    abstract fun bindOrderDetailsViewModel(viewModel: OrderDetailsViewModelImpl): ViewModel
}