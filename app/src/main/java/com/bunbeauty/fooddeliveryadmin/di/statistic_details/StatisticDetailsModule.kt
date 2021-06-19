package com.bunbeauty.fooddeliveryadmin.di.statistic_details

import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragmentArgs.fromBundle
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragment
import dagger.Module
import dagger.Provides

@Module
class StatisticDetailsModule {

    @Provides
    fun provideStatisticDetailsArgs(fragment: StatisticDetailsFragment) =
        fromBundle(fragment.requireArguments())
}