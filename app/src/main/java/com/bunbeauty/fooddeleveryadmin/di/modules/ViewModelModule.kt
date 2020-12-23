package com.bunbeauty.fooddeleveryadmin.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeleveryadmin.di.ViewModelKey
import com.bunbeauty.fooddeleveryadmin.view_model.MainViewModel
import com.bunbeauty.fooddeleveryadmin.view_model.base.ViewModelFactory
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
}