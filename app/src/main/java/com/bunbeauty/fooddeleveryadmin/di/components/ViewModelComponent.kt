package com.bunbeauty.fooddeleveryadmin.di.components

import androidx.lifecycle.ViewModelStoreOwner
import com.bunbeauty.fooddeleveryadmin.ui.main.MainActivity
import com.bunbeauty.fooddeleveryadmin.di.modules.ViewModelModule

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

    // dialogs
}