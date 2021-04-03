package com.bunbeauty.presentation.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.data.model.Time
import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

class StatisticViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val iDataStoreHelper: IDataStoreHelper
) : BaseViewModel() {

    val statisticField = ObservableField<List<Statistic>>()
    val isLoadingField = ObservableField(false)

    fun getStatistic(daysCount: Int, isAllCafes: Boolean, isAllTime: Boolean) {
        isLoadingField.set(true)
        viewModelScope.launch {
            val cafeId = iDataStoreHelper.cafeId.first()

            if (isAllCafes)
                apiRepository.getOrderWithCartProductsAllCafesList(daysCount)
                    .collect { ordersList ->
                        val statistics = arrayListOf<Statistic>()

                        statistics.addAll(ordersList.groupBy {
                            Time(it.timestamp, 3).toStringDateYYYYMMDD()
                        }.map { Statistic(it.key, it.key, it.value) }.sortedByDescending { it.date }.take(daysCount))

                        val orderListForAllStatistic = ordersList.groupBy {
                            Time(it.timestamp, 3).toStringDateYYYYMMDD()
                        }.toSortedMap().toList().takeLast(daysCount).flatMap { it.second }

                        statistics.add(0,Statistic(date = "Все время", orderList = orderListForAllStatistic))

                        statisticField.set(statistics)
                        withContext(Dispatchers.Main) {
                            isLoadingField.set(false)
                        }
                    }
            else
                apiRepository.getOrderWithCartProductsList(cafeId, daysCount)
                    .collect { ordersList ->
                        val statistics =
                            arrayListOf(Statistic(date = "Все время", orderList = ordersList))

                        statistics.addAll(ordersList.groupBy {
                            Time(it.timestamp, 3).toStringDateYYYYMMDD()
                        }.map { Statistic(it.key, it.key, it.value) })

                        statisticField.set(statistics)
                        withContext(Dispatchers.Main) {
                            isLoadingField.set(false)
                        }
                    }
        }
    }
}