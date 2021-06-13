package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.data.enums.Period
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.ui.adapter.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.StatisticItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class StatisticViewModel : BaseViewModel() {

    abstract val statisticState: StateFlow<State<List<StatisticItem>>>
    abstract var selectedAddressItem: AddressItem
    abstract var selectedPeriodItem: PeriodItem

    abstract fun getStatistic(cafeId: String?, period: String)
}

class StatisticViewModelImpl @Inject constructor(
    private val orderRepo: OrderRepo,
    resourcesProvider: IResourcesProvider
) : StatisticViewModel() {

    override val statisticState = MutableStateFlow<State<List<StatisticItem>>>(State.Loading())
    override var selectedAddressItem =
        AddressItem(resourcesProvider.getString(R.string.msg_statistic_all_cafes), null)
    override var selectedPeriodItem = PeriodItem(Period.DAY.text)

    override fun getStatistic(cafeId: String?, period: String) {
        statisticState.value = State.Loading()

        val orderMapFlow = if (cafeId == null) {
            when (period) {
                Period.DAY.text -> {
                    orderRepo.getAllCafeOrdersByDay()
                }
                Period.WEEK.text -> {
                    orderRepo.getAllCafeOrdersByWeek()
                }
                Period.MONTH.text -> {
                    orderRepo.getAllCafeOrdersByMonth()
                }
                else -> {
                    null
                }
            }
        } else {
            when (period) {
                Period.DAY.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndDay(cafeId)
                }
                Period.WEEK.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndWeek(cafeId)
                }
                Period.MONTH.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndMonth(cafeId)
                }
                else -> {
                    null
                }
            }
        }

        orderMapFlow?.onEach { orderMap ->
            statisticState.value = orderMap.map { orderEntry ->
                StatisticItem(orderEntry.key, orderEntry.value)
            }.toStateSuccess()
        }?.launchIn(viewModelScope)
    }
}