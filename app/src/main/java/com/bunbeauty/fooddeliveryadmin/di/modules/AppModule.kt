package com.bunbeauty.fooddeliveryadmin.di.modules

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ViewModelComponent::class])
class AppModule {

    @Provides
    fun provideLinearLayoutManager(context: Context) = LinearLayoutManager(context)
}