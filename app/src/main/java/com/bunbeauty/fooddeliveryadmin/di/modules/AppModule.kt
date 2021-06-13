package com.bunbeauty.fooddeliveryadmin.di.modules

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ActivityComponent::class])
class AppModule {

    @Provides
    fun provideLinearLayoutManager(context: Context) = LinearLayoutManager(context)
}