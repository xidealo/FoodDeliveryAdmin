package com.bunbeauty.presentation.di

import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.utils.ResourcesProvider
import com.bunbeauty.presentation.utils.StringUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Singleton
    @Binds
    abstract fun bindStringUtil(stringUtil: StringUtil): IStringUtil

    @Singleton
    @Binds
    abstract fun bindResourcesProvider(resourcesProvider: ResourcesProvider): IResourcesProvider

}