package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.Period.*
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.items.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.items.StatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticFragmentDirections.*
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val stringUtil: IStringUtil,
    private val orderUtil: IOrderUtil,
    dataStoreRepo: DataStoreRepo,
    resourcesProvider: IResourcesProvider,
) : BaseViewModel() {

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val statisticState: StateFlow<State<List<StatisticItem>>>
        get() = _statisticState
    private val _statisticState = MutableStateFlow<State<List<StatisticItem>>>(State.Loading())

    var selectedAddressItem =
        AddressItem(resourcesProvider.getString(R.string.msg_statistic_all_cafes), null)
    var selectedPeriodItem = PeriodItem(DAY.text)

    fun getStatistic(cafeId: String?, period: String) {
        _statisticState.value = State.Loading()

        val statisticListFlow = if (cafeId == null) {
            when (period) {
                DAY.text -> {
                    orderRepo.getAllCafeOrdersByDay()
                }
                WEEK.text -> {
                    orderRepo.getAllCafeOrdersByWeek()
                }
                MONTH.text -> {
                    orderRepo.getAllCafeOrdersByMonth()
                }
                else -> null
            }
        } else {
            when (period) {
                DAY.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndDay(cafeId)
                }
                WEEK.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndWeek(cafeId)
                }
                MONTH.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndMonth(cafeId)
                }
                else -> null
            }
        }

        statisticListFlow?.onEach { statisticList ->
            _statisticState.value = statisticList.map { statistic ->
                val proceeds = orderUtil.getProceeds(statistic.orderList, delivery)
                val proceedsString = stringUtil.getCostString(proceeds)
                StatisticItem(
                    statistic = statistic,
                    count = stringUtil.getOrderCountString(statistic.orderList.size),
                    proceeds = proceedsString
                )
            }.toStateSuccess()
        }?.launchIn(viewModelScope)
    }

    fun goToAddressList() {
        router.navigate(toStatisticAddressListBottomSheet())
    }

    fun goToPeriodList() {
        router.navigate(toStatisticPeriodListBottomSheet())
    }

    fun goToStatisticDetails(statistic: Statistic) {
        router.navigate(toStatisticDetailsFragment(statistic))
    }
}