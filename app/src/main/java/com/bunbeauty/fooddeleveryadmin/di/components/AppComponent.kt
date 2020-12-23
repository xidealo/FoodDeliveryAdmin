package com.bunbeauty.fooddeleveryadmin.di.components

import android.content.Context
import com.bunbeauty.fooddeleveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeleveryadmin.di.modules.ApiModule
import com.bunbeauty.fooddeleveryadmin.di.modules.AppModule
import com.bunbeauty.fooddeleveryadmin.di.modules.DataModule
import com.bunbeauty.fooddeleveryadmin.di.modules.RepositoryModule
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