package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.ApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import com.bunbeauty.fooddeliveryadmin.ui.statistic.StatisticNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class StatisticViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel<StatisticNavigator>() {
    override var navigator: WeakReference<StatisticNavigator>? = null

    fun getStatistic(daysCount: Int) {
        viewModelScope.launch {
            apiRepository.getOrderWithCartProductsList(daysCount).asFlow().collect { ordersList ->
                val statistics = ordersList.groupBy { Time(it.timestamp, 3).toStringDateYYYYMMDD() }
                var r = 0
            }
        }
    }

    fun getStatisticClick() {
        navigator?.get()?.getStatistic()
    }
}