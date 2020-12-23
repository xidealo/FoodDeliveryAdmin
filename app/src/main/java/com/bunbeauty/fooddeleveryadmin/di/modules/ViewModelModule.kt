package com.bunbeauty.fooddeleveryadmin.di.modules

import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeleveryadmin.view_model.base.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    /*@Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    internal abstract fun provideProductViewModel(productViewModel: ProductViewModel): ViewModel*/

}