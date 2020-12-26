package com.bunbeauty.fooddeliveryadmin.di.components

import android.content.Context
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.di.modules.ApiModule
import com.bunbeauty.fooddeliveryadmin.di.modules.AppModule
import com.bunbeauty.fooddeliveryadmin.di.modules.DataModule
import com.bunbeauty.fooddeliveryadmin.di.modules.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        DataModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun getViewModelComponent(): ViewModelComponent.Factory

    fun inject(application: FoodDeliveryAdminApplication)
}