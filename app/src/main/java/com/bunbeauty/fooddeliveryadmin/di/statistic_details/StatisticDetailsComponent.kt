package com.bunbeauty.fooddeliveryadmin.di.statistic_details

import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.StatusListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        StatisticDetailsBinding::class,
        StatisticDetailsModule::class
    ]
)
interface StatisticDetailsComponent {

    fun inject(fragment: StatisticDetailsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: StatisticDetailsFragment): StatisticDetailsComponent
    }
}