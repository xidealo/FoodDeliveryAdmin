package com.bunbeauty.fooddeliveryadmin

import android.app.Application
import com.bunbeauty.fooddeliveryadmin.di.components.AppComponent
import com.bunbeauty.fooddeliveryadmin.di.components.DaggerAppComponent

class FoodDeliveryAdminApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}