package com.bunbeauty.fooddeleveryadmin.di.modules

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunbeauty.fooddeleveryadmin.di.components.ViewModelComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ViewModelComponent::class])
class AppModule {

    @Provides
    fun provideLinearLayoutManager(context: Context) = LinearLayoutManager(context)
}