package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.ApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.Statistic
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.ui.statistic.StatisticNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

class StatisticViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel<StatisticNavigator>() {
    override var navigator: WeakReference<StatisticNavigator>? = null
    val statisticField = ObservableField<List<Statistic>>()
    val isLoadingField = ObservableField(false)

    fun getStatistic(daysCount: Int) {
        viewModelScope.launch {
            apiRepository.getOrderWithCartProductsList(daysCount).asFlow().collect { ordersList ->
                val statistics = ordersList.groupBy { Time(it.timestamp, 3).toStringDateYYYYMMDD() }
                    .map { Statistic(it.key, it.key, it.value) }
                statisticField.set(statistics)
                withContext(Dispatchers.Main){
                    isLoadingField.set(false)
                }
            }
        }
    }

    fun getStatisticClick() {
        isLoadingField.set(true)
        navigator?.get()?.getStatistic()
    }
}