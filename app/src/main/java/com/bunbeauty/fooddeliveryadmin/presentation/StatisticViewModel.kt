package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.data.model.Time
import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                apiRepository.getOrderWithCartProductsAllCafesList(daysCount).onEach {
                        ordersList ->
                    val statistics = arrayListOf<Statistic>()

                    statistics.addAll(ordersList.groupBy {
                        Time(it.timestamp, 3).toStringDateYYYYMMDD()
                    }.map { Statistic(it.key, it.key, it.value) }.sortedByDescending { it.date }.take(daysCount))

                    val orderListForAllStatistic = ordersList.groupBy {
                        Time(it.timestamp, 3).toStringDateYYYYMMDD()
                    }.toSortedMap().toList().takeLast(daysCount).flatMap { it.second }

                    statistics.add(0,Statistic(date = "Все время", orderList = orderListForAllStatistic))
                    //to check back users
                    /*val t = ordersList.groupBy { it.orderEntity.phone }.toSortedMap()
                    Log.d("asd", t.toString())*/

                    statisticField.set(statistics)
                    withContext(Dispatchers.Main) {
                        isLoadingField.set(false)
                    }
                }.catch {  }.collect()


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