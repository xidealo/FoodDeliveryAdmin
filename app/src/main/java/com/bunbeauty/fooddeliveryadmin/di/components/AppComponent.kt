package com.bunbeauty.fooddeliveryadmin.di.components

import android.content.Context
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.di.modules.*
import com.bunbeauty.fooddeliveryadmin.notification.MessagingService
import com.bunbeauty.fooddeliveryadmin.ui.view.NavigationCardView
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        DataModule::class,
        RepositoryModule::class,
        MapperModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun getActivityComponent(): ActivityComponent.Factory

    fun inject(application: FoodDeliveryAdminApplication)
    fun inject(messagingService: MessagingService)
    fun inject(navigationCardView: NavigationCardView)
}