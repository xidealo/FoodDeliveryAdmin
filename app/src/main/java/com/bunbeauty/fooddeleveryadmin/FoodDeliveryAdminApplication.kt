package com.bunbeauty.fooddeleveryadmin

import android.app.Application
import com.bunbeauty.fooddeleveryadmin.di.components.AppComponent
import com.bunbeauty.fooddeleveryadmin.di.components.DaggerAppComponent

class FoodDeliveryAdminApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}